package com.example.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.FollowupRequest;
import com.example.entities.ClosureReason;
import com.example.entities.Enquiry;
import com.example.entities.Followup;
import com.example.entities.Staff;
import com.example.repositories.ClosureReasonRepository;
import com.example.repositories.EnquiryRepository;
import com.example.repositories.FollowupRepository;
import com.example.repositories.StaffRepository;

@Service
public class FollowupServiceImpl implements FollowupService {

    @Autowired
    private FollowupRepository followupRepository;

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ClosureReasonRepository closureReasonRepository;

    @Override
    public List<Followup> getFollowupHistory(Integer enquiryId) {
        return followupRepository.findByEnquiryEnquiryIdOrderByFollowupDateDesc(enquiryId);
    }

    @Override
    public List<Followup> getHistoryForStaff(Integer staffId) {
        return followupRepository.findByStaffStaffIdOrderByFollowupDateDesc(staffId);
    }

    @Override
    public List<Followup> getAllFollowups() {
        return followupRepository.findAll();
    }

    @Override
    public Followup getFollowupById(Integer followupId) {
        return followupRepository.findById(followupId)
                .orElseThrow(() -> new NoSuchElementException("Follow-up not found: " + followupId));
    }

    @Override
    @Transactional
    public Followup logFollowup(FollowupRequest request, Integer staffId) {
        Enquiry enquiry = enquiryRepository.findById(request.getEnquiryId())
                .orElseThrow(() -> new NoSuchElementException("Enquiry not found: " + request.getEnquiryId()));

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new NoSuchElementException("Staff not found: " + staffId));

        Followup followup = new Followup(enquiry, staff, LocalDate.now(),
                request.getFollowupMsg(), true);
        followup = followupRepository.save(followup);

        int current = enquiry.getInquiryCounter() != null ? enquiry.getInquiryCounter() : 0;
        enquiry.setInquiryCounter(current + 1);

        if (request.getClosureReasonId() != null) {
            ClosureReason reason = closureReasonRepository.findById(request.getClosureReasonId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Closure reason not found: " + request.getClosureReasonId()));
            enquiry.setClosureReason(reason);
            enquiry.setClosureReasonText(request.getClosureReasonText());
            enquiry.setEnquiryProcessedFlag(true);
        } else {
            enquiry.setFollowupDate(
                    request.getNextFollowupDate() != null
                            ? request.getNextFollowupDate()
                            : LocalDate.now().plusDays(3));
        }

        enquiryRepository.save(enquiry);

        return followup;
    }

    @Override
    public List<Enquiry> getTodayForStaff(Integer staffId) {
        return enquiryRepository.findTodayDueForStaff(staffId, LocalDate.now());
    }

    @Override
    public List<Enquiry> getOverdueForStaff(Integer staffId) {
        return enquiryRepository.findOverdueForStaff(staffId, LocalDate.now());
    }
}