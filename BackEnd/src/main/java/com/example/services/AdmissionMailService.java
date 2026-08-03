package com.example.services;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.entities.Payment;
import com.example.entities.Receipt;
import com.example.entities.Student;

import jakarta.mail.internet.MimeMessage;

/**
 * Sends the "admission successful" email once a Razorpay payment has been
 * verified and the Student/Payment/Receipt rows are saved.
 *
 * Deliberately does NOT throw on failure - a misconfigured mail server (or
 * a temporary SMTP outage) must never undo an already-successful admission
 * and payment. The caller decides what to do with the boolean result (the
 * flow currently just reports it back to the frontend as emailSent=false).
 */
@Service
public class AdmissionMailService {

    private static final Logger log = LoggerFactory.getLogger(AdmissionMailService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    @Value("${app.mail.from-name:Computer Seekho}")
    private String fromName;

    public AdmissionMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @return true if the email was handed off to the SMTP server successfully.
     */
    public boolean sendAdmissionConfirmation(Student student, Payment payment, Receipt receipt) {
        String toAddress = student.getStudentEmail();
        if (toAddress == null || toAddress.isBlank()) {
            log.info("No email address on file for student {} - skipping admission email.", student.getStudentId());
            return false;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(toAddress);
            if (fromAddress != null && !fromAddress.isBlank()) {
                helper.setFrom(fromAddress, fromName);
            }
            helper.setSubject("Admission Confirmed - " + safe(student.getStudentName()) + " | " + fromName);
            helper.setText(buildHtmlBody(student, payment, receipt), true);

            mailSender.send(message);
            log.info("Admission confirmation email sent to {} for student {}", toAddress, student.getStudentId());
            return true;
        } catch (MailException | java.io.UnsupportedEncodingException | jakarta.mail.MessagingException e) {
            // Never let an email failure roll back an already-successful payment.
            log.warn("Failed to send admission confirmation email to {} for student {}: {}",
                    toAddress, student.getStudentId(), e.getMessage());
            return false;
        }
    }

    private String buildHtmlBody(Student student, Payment payment, Receipt receipt) {
        String courseName = payment.getCourse() != null ? payment.getCourse().getCourseName() : "-";
        String batchName = payment.getBatch() != null ? payment.getBatch().getBatchName() : "-";
        String amount = receipt.getReceiptAmount() != null ? "Rs. " + receipt.getReceiptAmount().toPlainString() : "-";
        String receiptDate = receipt.getReceiptDate() != null ? receipt.getReceiptDate().format(DATE_FMT) : "-";
        String studentDob = student.getStudentDob() != null ? student.getStudentDob().format(DATE_FMT) : "-";

        return "<div style=\"font-family:Arial,Helvetica,sans-serif;max-width:640px;margin:0 auto;color:#1f2937;\">"
                + "<h2 style=\"color:#0D9488;margin-bottom:4px;\">" + fromName + "</h2>"
                + "<p style=\"margin-top:0;\">Dear " + safe(student.getStudentName()) + ",</p>"
                + "<p>Congratulations! Your admission has been <b>confirmed</b> and your payment was received successfully."
                + " Please find your admission slip and fee receipt below - keep this email for your records.</p>"

                + "<h3 style=\"border-bottom:2px solid #0D9488;padding-bottom:6px;margin-top:28px;\">Admission Slip</h3>"
                + "<table style=\"width:100%;border-collapse:collapse;font-size:14px;\">"
                + row("Student Name", safe(student.getStudentName()))
                + row("Student ID", student.getStudentId() != null ? String.valueOf(student.getStudentId()) : "-")
                + row("Date of Birth", studentDob)
                + row("Mobile", student.getStudentMobile() != null ? String.valueOf(student.getStudentMobile()) : "-")
                + row("Course", courseName)
                + row("Batch", batchName)
                + "</table>"

                + "<h3 style=\"border-bottom:2px solid #0D9488;padding-bottom:6px;margin-top:28px;\">Fee Receipt</h3>"
                + "<table style=\"width:100%;border-collapse:collapse;font-size:14px;\">"
                + row("Receipt No.", receipt.getReceiptId() != null ? "RCPT-" + receipt.getReceiptId() : "-")
                + row("Receipt Date", receiptDate)
                + row("Payment Mode", payment.getPaymentType() != null ? payment.getPaymentType().getPaymentTypeDesc() : "Online (Razorpay)")
                + row("Razorpay Payment ID", payment.getRazorpayPaymentId() != null ? payment.getRazorpayPaymentId() : "-")
                + row("Amount Paid", amount)
                + "</table>"

                + "<p style=\"margin-top:28px;font-size:13px;color:#6b7280;\">"
                + "This is a system-generated email. For any queries regarding your admission or payment, "
                + "please contact the institute office and quote your Student ID / Receipt No.</p>"
                + "</div>";
    }

    private String row(String label, String value) {
        return "<tr>"
                + "<td style=\"padding:6px 10px;background:#f3f4f6;font-weight:bold;width:40%;border:1px solid #e5e7eb;\">" + label + "</td>"
                + "<td style=\"padding:6px 10px;border:1px solid #e5e7eb;\">" + value + "</td>"
                + "</tr>";
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }
}
