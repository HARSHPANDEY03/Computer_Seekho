package com.example.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.dto.DashboardSummaryResponse;
import com.example.repositories.EnquiryRepository;
import com.example.repositories.FollowupRepository;
import com.example.repositories.PaymentRepository;
import com.example.repositories.StudentRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final EnquiryRepository enquiryRepository;
    private final FollowupRepository followupRepository;
    private final StudentRepository studentRepository;
//    private final PaymentRepository paymentRepository;

    // Constructor Injection
    public DashboardServiceImpl(
            EnquiryRepository enquiryRepository,
            FollowupRepository followupRepository,
            StudentRepository studentRepository/* ,
           PaymentRepository paymentRepository*/) {

        this.enquiryRepository = enquiryRepository;
        this.followupRepository = followupRepository;
        this.studentRepository = studentRepository;
//        this.paymentRepository = paymentRepository;
    }

    @Override
    public DashboardSummaryResponse getDashboardSummary() {

        // 1. Total Enquiries
        Long totalEnquiries = enquiryRepository.count();

        // 2. Today's Follow-ups
        LocalDate today = LocalDate.now();

        Long todayFollowups =
                followupRepository.countByFollowupDate(today);

        // 3. Total Admissions
        // Every Student record represents an admission
        Long totalAdmissions = studentRepository.count();

        // 4. Total Fees Received
//        Double totalFeesReceived =
//                paymentRepository.getTotalFeesReceived();
//
//        // Prevent null when there are no payments
//        if (totalFeesReceived == null) {
//            totalFeesReceived = 0.0;
//        }

        // 5. Pending Fees
//        Double pendingFees =
//                paymentRepository.getPendingFees();
//
//        // Prevent null when there are no pending fees
//        if (pendingFees == null) {
//            pendingFees = 0.0;
//        }

        return new DashboardSummaryResponse(
                totalEnquiries,
                todayFollowups,
                totalAdmissions
//                totalFeesReceived,
//                pendingFees
        );
    }
}