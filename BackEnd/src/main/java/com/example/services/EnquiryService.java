package com.example.services;

import com.example.dto.AssignStaffRequest;
import com.example.entities.Enquiry;
import java.util.List;
import java.util.Optional;

public interface EnquiryService {
	List<Enquiry> getAllEnquiries();

	Optional<Enquiry> getEnquiryById(Integer id);

	Enquiry saveEnquiry(Enquiry enquiry);

	void deleteEnquiry(Integer id);

	// Add this method signature here
	Enquiry assignStaff(Integer enquiryId, AssignStaffRequest request);
}