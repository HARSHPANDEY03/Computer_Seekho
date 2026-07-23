package com.example.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Followup;
import com.example.repositories.FollowupRepository;

@Service
public class FollowupServiceImpl implements FollowupService{

	@Autowired
	private FollowupRepository followupRepository;
	
	@Override
	 public List<Followup> getFollowupHistory(Integer enquiryId) {
        return followupRepository.findByEnquiryEnquiryIdOrderByFollowupDateDesc(enquiryId);
    }
}
	