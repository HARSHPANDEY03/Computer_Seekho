package com.example.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.config.RazorpayConfig;
import com.example.dto.CreateOrderRequest;
import com.example.dto.CreateOrderResponse;
import com.example.dto.PaymentHistoryItemResponse;
import com.example.dto.PaymentReceiptResponse;
import com.example.dto.PaymentSummaryResponse;
import com.example.dto.VerifyPaymentRequest;
import com.example.entities.Batch;
import com.example.entities.Course;
import com.example.entities.Enquiry;
import com.example.entities.Payment;
import com.example.entities.PaymentType;
import com.example.entities.Receipt;
import com.example.entities.Student;
import com.example.exceptions.DuplicateAdmissionException;
import com.example.exceptions.PaymentVerificationException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.repositories.BatchRepository;
import com.example.repositories.CourseRepository;
import com.example.repositories.EnquiryRepository;
import com.example.repositories.PaymentRepository;
import com.example.repositories.PaymentTypeRepository;
import com.example.repositories.ReceiptRepository;
import com.example.repositories.StudentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

/**
 * Handles student-admission payments, online (Razorpay) and offline
 * (Cash / Bank), with support for paying the course fee across any number
 * of installments of any amount:
 *
 *   FIRST PAYMENT (studentId absent on the request):
 *   - Admits the student (creates the Student row) using the FULL
 *     course/batch fee as courseFee - that's the total owed, not
 *     necessarily what's being paid right now.
 *   - The amount actually charged this time may be less than the full fee
 *     (the admin's chosen first installment). Defaults to the full fee if
 *     no amount is given, preserving the old single-payment behaviour.
 *
 *   FOLLOW-UP INSTALLMENT (studentId present on the request):
 *   - No new Student row - reuses the existing one.
 *   - Amount is validated against the remaining pending balance
 *     (courseFee minus the sum of all PAID payments so far) and rejected
 *     if it would overpay.
 *
 * Every payment - first or installment - still produces its own Payment +
 * Receipt row, so the full installment history is just "all Payment rows
 * for this student", and the running balance is a live sum, not a stored
 * counter that could drift.
 */
@Service
public class RazorpayPaymentService {

    private static final Logger log = LoggerFactory.getLogger(RazorpayPaymentService.class);

    // Must match a row in the paymentType table - PaymentTypeDataInitializer
    // creates it automatically on startup if missing.
    public static final String ONLINE_PAYMENT_TYPE_DESC = "Online (Razorpay)";

    private final RazorpayClient razorpayClient;
    private final RazorpayConfig razorpayConfig;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;
    private final StudentRepository studentRepository;
    private final EnquiryRepository enquiryRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final ReceiptRepository receiptRepository;
    private final AdmissionMailService admissionMailService;
    private final String razorpayPublicKeyId;

    public RazorpayPaymentService(RazorpayClient razorpayClient,
                                   RazorpayConfig razorpayConfig,
                                   CourseRepository courseRepository,
                                   BatchRepository batchRepository,
                                   StudentRepository studentRepository,
                                   EnquiryRepository enquiryRepository,
                                   PaymentRepository paymentRepository,
                                   PaymentTypeRepository paymentTypeRepository,
                                   ReceiptRepository receiptRepository,
                                   AdmissionMailService admissionMailService,
                                   @Qualifier("razorpayKeyId") String razorpayKeyId) {
        this.razorpayClient = razorpayClient;
        this.razorpayConfig = razorpayConfig;
        this.courseRepository = courseRepository;
        this.batchRepository = batchRepository;
        this.studentRepository = studentRepository;
        this.enquiryRepository = enquiryRepository;
        this.paymentRepository = paymentRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.receiptRepository = receiptRepository;
        this.admissionMailService = admissionMailService;
        this.razorpayPublicKeyId = razorpayKeyId;
    }

    /**
     * Step 1: create a Razorpay order.
     *
     * - First payment, no amount given: full course/batch fee (old behaviour).
     * - First payment, amount given: that becomes the first installment -
     *   must not exceed the full fee.
     * - Installment (studentId given): amount is required and must not
     *   exceed the student's remaining pending balance.
     *
     * Either way the amount is always validated server-side against a real
     * number from the database - never trusted outright from the browser.
     */
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) throws RazorpayException {
        if (request.getCourseId() == null) {
            throw new IllegalArgumentException("courseId is required");
        }

        // First payment for an enquiry that's already admitted - reject
        // BEFORE creating a Razorpay order, not after the student has
        // already been charged. (verifyAndPersist below has its own copy
        // of this same check as a final safety net, but by then it's too
        // late to avoid charging someone for nothing.)
        if (request.getStudentId() == null && request.getEnquiryId() != null
                && studentRepository.existsByEnquiryEnquiryId(request.getEnquiryId())) {
            throw new DuplicateAdmissionException(
                    "Student is already registered for enquiry ID: " + request.getEnquiryId());
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.getCourseId()));

        if (course.getCourseFees() == null) {
            throw new IllegalStateException("Course " + course.getCourseId() + " has no fee configured");
        }

        Batch batch = null;
        if (request.getBatchId() != null) {
            batch = batchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + request.getBatchId()));

            if (batch.getCourse() == null || batch.getCourse().getCourseId() == null
                    || !batch.getCourse().getCourseId().equals(course.getCourseId())) {
                throw new IllegalArgumentException("Selected batch does not belong to the selected course");
            }
        }

        BigDecimal fullFee = (batch != null && batch.getCourseFees() != null)
                ? batch.getCourseFees()
                : course.getCourseFees();

        Student existingStudent = null;
        BigDecimal chargeAmount;

        if (request.getStudentId() != null) {
            existingStudent = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

            if (request.getAmount() == null || request.getAmount() <= 0) {
                throw new IllegalArgumentException("amount is required and must be greater than zero for an installment payment");
            }
            chargeAmount = BigDecimal.valueOf(request.getAmount());

            BigDecimal pending = pendingBalance(existingStudent);
            if (chargeAmount.compareTo(pending) > 0) {
                throw new IllegalArgumentException(
                        "Amount (" + chargeAmount + ") exceeds the remaining pending balance (" + pending + ")");
            }
        } else if (request.getAmount() != null && request.getAmount() > 0) {
            chargeAmount = BigDecimal.valueOf(request.getAmount());
            if (chargeAmount.compareTo(fullFee) > 0) {
                throw new IllegalArgumentException(
                        "Amount (" + chargeAmount + ") exceeds the course/batch fee (" + fullFee + ")");
            }
        } else {
            chargeAmount = fullFee;
        }

        long amountInPaise = chargeAmount
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "course_" + course.getCourseId() + "_" + System.currentTimeMillis());
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);
        String razorpayOrderId = order.get("id");

        // Pre-create a Payment row in CREATED state so abandoned/failed
        // checkouts can be reconciled later if needed. For an installment,
        // the student is already known, so link it immediately.
        Payment payment = new Payment();
        payment.setCourse(course);
        payment.setBatch(batch);
        payment.setAmount(chargeAmount.doubleValue());
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setStatus("CREATED");
        if (existingStudent != null) {
            payment.setStudent(existingStudent);
        }
        paymentRepository.save(payment);

        log.info("Created Razorpay order {} for course {} (amount {})",
                razorpayOrderId, course.getCourseId(), chargeAmount);

        return new CreateOrderResponse(
                razorpayOrderId,
                amountInPaise,
                "INR",
                razorpayPublicKeyId,
                course.getCourseId(),
                request.getBatchId(),
                course.getCourseName()
        );
    }

    /**
     * Step 2: called after Razorpay checkout succeeds on the frontend.
     * Verifies the signature server-side (mandatory - this is the only way
     * to actually trust the payment), then either admits a new student
     * (first payment) or attaches this payment to an existing one
     * (installment), and generates the Receipt, all in one transaction.
     */
    @Transactional
    public PaymentReceiptResponse verifyAndPersist(VerifyPaymentRequest req) {
        if (req.getRazorpayOrderId() == null || req.getRazorpayPaymentId() == null
                || req.getRazorpaySignature() == null) {
            throw new IllegalArgumentException(
                    "razorpayOrderId, razorpayPaymentId and razorpaySignature are all required");
        }

        Payment payment = paymentRepository.findByRazorpayOrderId(req.getRazorpayOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No order found for razorpay_order_id=" + req.getRazorpayOrderId()));

        // Idempotency guard: if this order was already verified (e.g. the
        // browser retried the request), just return the existing receipt
        // instead of creating a duplicate student/payment/receipt.
        if ("PAID".equals(payment.getStatus())) {
            Receipt existing = receiptRepository.findByPayment_PaymentId(payment.getPaymentId())
                    .orElseThrow(() -> new IllegalStateException("Payment marked PAID but receipt missing"));
            return toReceiptResponse(existing, false);
        }

        Map<String, String> options = new HashMap<>();
        options.put("razorpay_order_id", req.getRazorpayOrderId());
        options.put("razorpay_payment_id", req.getRazorpayPaymentId());
        options.put("razorpay_signature", req.getRazorpaySignature());

        boolean valid;
        try {
            valid = Utils.verifyPaymentSignature(new JSONObject(options), razorpayConfig.getKeySecret());
        } catch (RazorpayException e) {
            log.warn("Signature verification threw for order {}: {}", req.getRazorpayOrderId(), e.getMessage());
            valid = false;
        }

        if (!valid) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            log.warn("Signature verification FAILED for order {}", req.getRazorpayOrderId());
            throw new PaymentVerificationException("Payment signature verification failed");
        }

        // --- Signature confirmed genuine: safe to persist now ---
        Student student;

        if (req.getStudentId() != null) {
            // Installment - reuse the existing student, no admission logic.
            student = studentRepository.findById(req.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + req.getStudentId()));
        } else {
            // First payment - admits the student.
            Enquiry enquiry = null;
            if (req.getEnquiryId() != null) {
                enquiry = enquiryRepository.findById(req.getEnquiryId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Enquiry not found with ID: " + req.getEnquiryId()));

                if (studentRepository.existsByEnquiryEnquiryId(req.getEnquiryId())) {
                    throw new DuplicateAdmissionException(
                            "Student is already registered for enquiry ID: " + req.getEnquiryId());
                }
            }

            // Prefer the email typed into the admission form; fall back to
            // the one already on file for the enquiry (if linked).
            String resolvedEmail = req.getStudentEmail();
            if ((resolvedEmail == null || resolvedEmail.isBlank()) && enquiry != null) {
                resolvedEmail = enquiry.getEnquirerEmailId();
            }

            Student newStudent = new Student();
            newStudent.setEnquiry(enquiry);
            newStudent.setStudentName(req.getStudentName());
            newStudent.setStudentAddress(req.getStudentAddress());
            newStudent.setStudentGender(req.getStudentGender());
            newStudent.setStudentDob(req.getStudentDob());
            newStudent.setStudentQualification(req.getStudentQualification());
            newStudent.setStudentMobile(req.getStudentMobile());
            newStudent.setStudentEmail(resolvedEmail);
            newStudent.setPhotoUrl(req.getPhotoUrl());
            newStudent.setCourse(payment.getCourse());
            newStudent.setBatch(payment.getBatch());

            // IMPORTANT: courseFee on the student is always the FULL fee
            // owed, not just this installment's amount - otherwise future
            // installments would have nothing correct to be measured
            // against. (Previously this stored payment.getAmount(), which
            // only worked because every payment used to equal the full fee.)
            BigDecimal fullFee = (payment.getBatch() != null && payment.getBatch().getCourseFees() != null)
                    ? payment.getBatch().getCourseFees()
                    : payment.getCourse().getCourseFees();
            newStudent.setCourseFee(fullFee);

            student = studentRepository.save(newStudent);

            if (enquiry != null) {
                enquiry.setEnquiryProcessedFlag(true);
                enquiryRepository.save(enquiry);
            }
        }

        PaymentType onlineType = paymentTypeRepository.findByPaymentTypeDescIgnoreCase(ONLINE_PAYMENT_TYPE_DESC)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "payment_type row '" + ONLINE_PAYMENT_TYPE_DESC + "' not found"));

        payment.setStudent(student);
        payment.setPaymentType(onlineType);
        payment.setPaymentDate(LocalDate.now());
        payment.setRazorpayPaymentId(req.getRazorpayPaymentId());
        payment.setStatus("PAID");
        payment.setCollectedBy(currentUsername());
        payment.setRemarks(req.getRemarks());
        payment = paymentRepository.save(payment);

        Receipt receipt = new Receipt();
        receipt.setReceiptDate(LocalDate.now());
        receipt.setReceiptAmount(BigDecimal.valueOf(payment.getAmount()));
        receipt.setPayment(payment);
        receipt.setStudent(student);
        receipt.setCourse(payment.getCourse());
        receipt = receiptRepository.save(receipt);

        log.info("Payment {} verified and receipt {} generated for student {}",
                payment.getPaymentId(), receipt.getReceiptId(), student.getStudentId());

        // Best-effort - a failed/unconfigured mail server must never undo an
        // already-successful payment. See AdmissionMailService.
        boolean emailSent = admissionMailService.sendAdmissionConfirmation(student, payment, receipt);

        return toReceiptResponse(receipt, emailSent);
    }

    /**
     * Offline (Cash / Bank) payment: no gateway step, so there's nothing to
     * verify. Same first-payment-vs-installment branching as the online
     * flow above, and the same courseFee / pending-balance rules.
     */
    @Transactional
    public PaymentReceiptResponse recordOfflinePayment(VerifyPaymentRequest req) {
        if (req.getPaymentType() == null || req.getPaymentType().isBlank()) {
            throw new IllegalArgumentException("paymentType is required");
        }
        if (req.getAmount() == null || req.getAmount() <= 0) {
            throw new IllegalArgumentException("amount is required and must be greater than zero");
        }

        Student student;
        Course course;
        Batch batch;
        BigDecimal amount = BigDecimal.valueOf(req.getAmount());

        if (req.getStudentId() != null) {
            student = studentRepository.findById(req.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + req.getStudentId()));
            course = student.getCourse();
            batch = student.getBatch();

            BigDecimal pending = pendingBalance(student);
            if (amount.compareTo(pending) > 0) {
                throw new IllegalArgumentException(
                        "Amount (" + amount + ") exceeds the remaining pending balance (" + pending + ")");
            }
        } else {
            if (req.getCourseId() == null) {
                throw new IllegalArgumentException("courseId is required");
            }
            course = courseRepository.findById(req.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + req.getCourseId()));

            batch = null;
            if (req.getBatchId() != null) {
                batch = batchRepository.findById(req.getBatchId())
                        .orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + req.getBatchId()));
            }

            BigDecimal fullFee = (batch != null && batch.getCourseFees() != null)
                    ? batch.getCourseFees()
                    : course.getCourseFees();
            if (fullFee != null && amount.compareTo(fullFee) > 0) {
                throw new IllegalArgumentException(
                        "Amount (" + amount + ") exceeds the course/batch fee (" + fullFee + ")");
            }

            Enquiry enquiry = null;
            if (req.getEnquiryId() != null) {
                enquiry = enquiryRepository.findById(req.getEnquiryId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Enquiry not found with ID: " + req.getEnquiryId()));

                if (studentRepository.existsByEnquiryEnquiryId(req.getEnquiryId())) {
                    throw new DuplicateAdmissionException(
                            "Student is already registered for enquiry ID: " + req.getEnquiryId());
                }
            }

            String resolvedEmail = req.getStudentEmail();
            if ((resolvedEmail == null || resolvedEmail.isBlank()) && enquiry != null) {
                resolvedEmail = enquiry.getEnquirerEmailId();
            }

            Student newStudent = new Student();
            newStudent.setEnquiry(enquiry);
            newStudent.setStudentName(req.getStudentName());
            newStudent.setStudentAddress(req.getStudentAddress());
            newStudent.setStudentGender(req.getStudentGender());
            newStudent.setStudentDob(req.getStudentDob());
            newStudent.setStudentQualification(req.getStudentQualification());
            newStudent.setStudentMobile(req.getStudentMobile());
            newStudent.setStudentEmail(resolvedEmail);
            newStudent.setPhotoUrl(req.getPhotoUrl());
            newStudent.setCourse(course);
            newStudent.setBatch(batch);
            // Same rule as the online flow: store the FULL fee owed, not
            // just this first installment's amount.
            newStudent.setCourseFee(fullFee);
            student = studentRepository.save(newStudent);

            if (enquiry != null) {
                enquiry.setEnquiryProcessedFlag(true);
                enquiryRepository.save(enquiry);
            }
        }

        PaymentType paymentType = paymentTypeRepository.findByPaymentTypeDescIgnoreCase(req.getPaymentType())
                .orElseGet(() -> {
                    PaymentType pt = new PaymentType();
                    pt.setPaymentTypeDesc(req.getPaymentType());
                    return paymentTypeRepository.save(pt);
                });

        Payment payment = new Payment();
        payment.setCourse(course);
        payment.setBatch(batch);
        payment.setAmount(req.getAmount());
        payment.setStudent(student);
        payment.setPaymentType(paymentType);
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus("PAID");
        payment.setCollectedBy(currentUsername());
        payment.setRemarks(req.getRemarks());
        payment = paymentRepository.save(payment);

        Receipt receipt = new Receipt();
        receipt.setReceiptDate(LocalDate.now());
        receipt.setReceiptAmount(BigDecimal.valueOf(payment.getAmount()));
        receipt.setPayment(payment);
        receipt.setStudent(student);
        receipt.setCourse(course);
        receipt = receiptRepository.save(receipt);

        log.info("Offline ({}) payment {} recorded, receipt {} generated for student {}",
                req.getPaymentType(), payment.getPaymentId(), receipt.getReceiptId(), student.getStudentId());

        boolean emailSent = admissionMailService.sendAdmissionConfirmation(student, payment, receipt);

        return toReceiptResponse(receipt, emailSent);
    }

    /**
     * Live pending-balance summary for a student: course fee, total PAID so
     * far, remaining balance, and whether it's fully settled. Nothing is
     * stored/cached - it's always a fresh sum over the Payment table, so it
     * can never drift out of sync with the actual payment history.
     */
    @Transactional(readOnly = true)
    public PaymentSummaryResponse getPaymentSummary(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

        BigDecimal fee = student.getCourseFee() != null ? student.getCourseFee() : BigDecimal.ZERO;
        BigDecimal pending = pendingBalance(student);
        BigDecimal paid = fee.subtract(pending);

        return new PaymentSummaryResponse(
                student.getStudentId(),
                student.getStudentName(),
                fee,
                paid,
                pending,
                pending.compareTo(BigDecimal.ZERO) <= 0
        );
    }

    /**
     * Every payment attempt for a student, oldest first - including
     * abandoned/failed ones, so this is a genuine audit trail rather than
     * just a receipts list. For each row:
     *  - remainingFeeAfter is the running balance immediately after this
     *    row (only successful payments reduce it - a Pending or Failed
     *    attempt leaves the balance exactly where it was).
     *  - receiptId is only present for successful payments - Pending and
     *    Failed rows never had a receipt generated for them.
     */
    @Transactional(readOnly = true)
    public List<PaymentHistoryItemResponse> getPaymentHistory(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

        BigDecimal courseFee = student.getCourseFee() != null ? student.getCourseFee() : BigDecimal.ZERO;

        List<Payment> payments = paymentRepository.findByStudentStudentId(studentId).stream()
                .sorted(Comparator
                        .comparing(Payment::getPaymentDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Payment::getPaymentId))
                .toList();

        List<PaymentHistoryItemResponse> history = new ArrayList<>();
        BigDecimal cumulativePaid = BigDecimal.ZERO;

        for (Payment p : payments) {
            boolean succeeded = isSuccess(p.getStatus());
            if (succeeded) {
                cumulativePaid = cumulativePaid.add(BigDecimal.valueOf(p.getAmount() != null ? p.getAmount() : 0));
            }
            BigDecimal remainingAfter = courseFee.subtract(cumulativePaid).max(BigDecimal.ZERO);

            Integer receiptId = null;
            if (succeeded) {
                Optional<Receipt> receipt = receiptRepository.findByPayment_PaymentId(p.getPaymentId());
                if (receipt.isPresent()) {
                    receiptId = receipt.get().getReceiptId();
                }
            }

            String mode = p.getPaymentType() != null
                    ? p.getPaymentType().getPaymentTypeDesc()
                    : (p.getRazorpayPaymentId() != null || p.getRazorpayOrderId() != null ? "Online (Razorpay)" : "—");

            history.add(new PaymentHistoryItemResponse(
                    p.getPaymentId(),
                    receiptId,
                    p.getPaymentDate(),
                    p.getAmount(),
                    remainingAfter,
                    courseFee,
                    mode,
                    p.getRazorpayPaymentId(),
                    statusLabel(p.getStatus()),
                    p.getCollectedBy(),
                    p.getRemarks()
            ));
        }

        return history;
    }

    private String statusLabel(String rawStatus) {
        if (isSuccess(rawStatus)) return "Success";
        if ("FAILED".equals(rawStatus)) return "Failed";
        if ("CREATED".equals(rawStatus)) return "Pending";
        return rawStatus;
    }

    // Legacy rows created before the `status` column existed on this table
    // are left NULL - back then, a row existing at all meant it succeeded
    // (there was no CREATED/FAILED concept yet). Treat NULL the same as
    // PAID everywhere a "did this payment actually go through" check is
    // made, so old payments aren't silently dropped from totals/history.
    private boolean isSuccess(String status) {
        return status == null || "PAID".equals(status);
    }

    // Username of the currently authenticated staff member (from the JWT),
    // or null if the request wasn't authenticated. Used to record who
    // collected each payment.
    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() != null
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return null;
    }

    private BigDecimal pendingBalance(Student student) {
        BigDecimal fee = student.getCourseFee() != null ? student.getCourseFee() : BigDecimal.ZERO;
        Double paid = paymentRepository.sumPaidAmountByStudent(student.getStudentId());
        BigDecimal paidAmount = BigDecimal.valueOf(paid != null ? paid : 0.0);
        BigDecimal pending = fee.subtract(paidAmount);
        return pending.max(BigDecimal.ZERO);
    }

    private PaymentReceiptResponse toReceiptResponse(Receipt receipt, boolean emailSent) {
        Student student = receipt.getStudent();
        Payment payment = receipt.getPayment();
        Batch batch = payment != null ? payment.getBatch() : null;

        return new PaymentReceiptResponse(
                receipt.getReceiptId(),
                receipt.getReceiptDate(),
                receipt.getReceiptAmount(),
                student != null ? student.getStudentId() : null,
                student != null ? student.getStudentName() : null,
                student != null ? student.getStudentEmail() : null,
                student != null ? student.getStudentMobile() : null,
                receipt.getCourse() != null ? receipt.getCourse().getCourseId() : null,
                receipt.getCourse() != null ? receipt.getCourse().getCourseName() : null,
                batch != null ? batch.getBatchId() : null,
                batch != null ? batch.getBatchName() : null,
                payment != null ? payment.getRazorpayPaymentId() : null,
                payment != null ? payment.getPaymentId() : null,
                emailSent
        );
    }
}