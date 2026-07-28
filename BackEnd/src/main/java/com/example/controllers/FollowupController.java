package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.FollowupRequest;
import com.example.entities.Enquiry;
import com.example.entities.Followup;
import com.example.services.FollowupService;

@RestController
@RequestMapping("/api/followups")
public class FollowupController {

    @Autowired
    private FollowupService followupService;

    @GetMapping("/enquiry/{enquiryId}")
    public List<Followup> getFollowupHistory(@PathVariable Integer enquiryId) {
        return followupService.getFollowupHistory(enquiryId);
    }

    @GetMapping("/staff/{staffId}")
    public List<Followup> getHistoryForStaff(@PathVariable Integer staffId) {
        return followupService.getHistoryForStaff(staffId);
    }

    @GetMapping
    public List<Followup> getAllFollowups() {
        return followupService.getAllFollowups();
    }

    @GetMapping("/{id}")
    public Followup getFollowupById(@PathVariable Integer id) {
        return followupService.getFollowupById(id);
    }

    @PostMapping
    public ResponseEntity<Followup> logFollowup(
            @RequestBody FollowupRequest request,
            @RequestParam Integer staffId) {
        Followup saved = followupService.logFollowup(request, staffId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/today")
    public List<Enquiry> today(@RequestParam Integer staffId) {
        return followupService.getTodayForStaff(staffId);
    }

    @GetMapping("/overdue")
    public List<Enquiry> overdue(@RequestParam Integer staffId) {
        return followupService.getOverdueForStaff(staffId);
    }

    @GetMapping("/staff/{staffId}/today")
    public List<Enquiry> todayForStaff(@PathVariable Integer staffId) {
        return followupService.getTodayForStaff(staffId);
    }

    @GetMapping("/staff/{staffId}/overdue")
    public List<Enquiry> overdueForStaff(@PathVariable Integer staffId) {
        return followupService.getOverdueForStaff(staffId);
    }
}