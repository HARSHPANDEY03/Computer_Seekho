package com.example.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
}
