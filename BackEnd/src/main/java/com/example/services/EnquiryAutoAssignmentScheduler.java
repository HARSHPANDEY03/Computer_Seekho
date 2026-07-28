package com.example.services;

import com.example.entities.Enquiry;
import com.example.entities.Staff;
import com.example.repositories.EnquiryRepository;
import com.example.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class EnquiryAutoAssignmentScheduler {

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private StaffRepository staffRepository;

    // Runs every 1 hour (3600000 milliseconds)
    @Scheduled(fixedRate = 3600000)
    public void autoAssignDefaultStaff() {
        LocalDate oneDayAgo = LocalDate.now().minusDays(1);
        List<Enquiry> unassignedList = enquiryRepository.findUnassignedOlderThan(oneDayAgo);

        if (!unassignedList.isEmpty()) {
            Integer defaultStaffId = 1; // Replace with your default system/general staff ID
            Staff defaultStaff = staffRepository.findById(defaultStaffId).orElse(null);

            if (defaultStaff != null) {
                for (Enquiry enquiry : unassignedList) {
                    enquiry.setStaff(defaultStaff);
                    enquiryRepository.save(enquiry);
                }
            }
        }
    }
}