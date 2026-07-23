package com.example.services;
import java.util.List;
import com.example.entities.Followup;
public interface FollowupService {
	List<Followup> getFollowupHistory(Integer enquiryId);
}
