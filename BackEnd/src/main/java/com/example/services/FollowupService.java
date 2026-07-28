package com.example.services;

import java.util.List;

import com.example.dto.FollowupRequest;
import com.example.entities.Enquiry;
import com.example.entities.Followup;

public interface FollowupService {

    List<Followup> getFollowupHistory(Integer enquiryId);

    List<Followup> getHistoryForStaff(Integer staffId);

    List<Followup> getAllFollowups();

    Followup getFollowupById(Integer followupId);

    Followup logFollowup(FollowupRequest request, Integer staffId);

    List<Enquiry> getTodayForStaff(Integer staffId);

    List<Enquiry> getOverdueForStaff(Integer staffId);
}