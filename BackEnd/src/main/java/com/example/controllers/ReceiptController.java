// ReceiptController.java
package com.example.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ReceiptDetailResponse;
import com.example.entities.Batch;
import com.example.entities.Course;
import com.example.entities.Payment;
import com.example.entities.Receipt;
import com.example.entities.Student;
import com.example.services.ReceiptService;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<ReceiptDetailResponse> getReceiptById(@PathVariable("receiptId") Integer receiptId) {
        Receipt receipt = receiptService.getReceiptById(receiptId);
        return ResponseEntity.ok(toDetailResponse(receipt));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReceiptDetailResponse>> getReceiptsByStudentName(
            @RequestParam("studentName") String studentName) {
        List<Receipt> receipts = receiptService.getReceiptsByStudentName(studentName);
        return ResponseEntity.ok(receipts.stream().map(this::toDetailResponse).toList());
    }

    /**
     * Flattens the entity graph into a DTO before it ever reaches Jackson.
     * The raw Receipt entity can't be serialized directly: Course has a
     * @OneToMany List<Batch>, Batch has a @ManyToOne back to Course, and
     * neither side breaks that cycle for JSON - serializing it directly
     * crashes with "Document nesting depth exceeds the maximum allowed"
     * for any course that has batches (i.e. almost always).
     */
    private ReceiptDetailResponse toDetailResponse(Receipt receipt) {
        ReceiptDetailResponse dto = new ReceiptDetailResponse();
        dto.setReceiptId(receipt.getReceiptId());
        dto.setReceiptDate(receipt.getReceiptDate());
        dto.setReceiptAmount(receipt.getReceiptAmount());

        Student student = receipt.getStudent();
        if (student != null) {
            ReceiptDetailResponse.StudentInfo s = new ReceiptDetailResponse.StudentInfo();
            s.setStudentId(student.getStudentId());
            s.setStudentName(student.getStudentName());
            s.setStudentAddress(student.getStudentAddress());
            s.setStudentGender(student.getStudentGender());
            s.setStudentDob(student.getStudentDob());
            s.setStudentMobile(student.getStudentMobile());
            s.setStudentEmail(student.getStudentEmail());
            dto.setStudent(s);
        }

        Course topLevelCourse = receipt.getCourse();
        if (topLevelCourse != null) {
            dto.setCourse(toCourseInfo(topLevelCourse));
        }

        Payment payment = receipt.getPayment();
        if (payment != null) {
            ReceiptDetailResponse.PaymentInfo p = new ReceiptDetailResponse.PaymentInfo();
            p.setPaymentId(payment.getPaymentId());
            p.setAmount(payment.getAmount());
            p.setPaymentDate(payment.getPaymentDate());
            p.setStatus(payment.getStatus());
            p.setRazorpayPaymentId(payment.getRazorpayPaymentId());

            Course paymentCourse = payment.getCourse();
            if (paymentCourse != null) {
                p.setCourse(toCourseInfo(paymentCourse));
                if (dto.getCourse() == null) {
                    dto.setCourse(toCourseInfo(paymentCourse));
                }
            }

            Batch batch = payment.getBatch();
            if (batch != null) {
                ReceiptDetailResponse.BatchInfo b = new ReceiptDetailResponse.BatchInfo();
                b.setBatchId(batch.getBatchId());
                b.setBatchName(batch.getBatchName());
                p.setBatch(b);
            }

            if (payment.getPaymentType() != null) {
                ReceiptDetailResponse.PaymentTypeInfo pt = new ReceiptDetailResponse.PaymentTypeInfo();
                pt.setPaymentTypeDesc(payment.getPaymentType().getPaymentTypeDesc());
                p.setPaymentType(pt);
            }

            dto.setPayment(p);
        }

        return dto;
    }

    private ReceiptDetailResponse.CourseInfo toCourseInfo(Course course) {
        ReceiptDetailResponse.CourseInfo c = new ReceiptDetailResponse.CourseInfo();
        c.setCourseId(course.getCourseId());
        c.setCourseName(course.getCourseName());
        return c;
    }
}