package com.example.services;

import com.example.dto.AssignStaffRequest;
import com.example.entities.Enquiry;
import com.example.entities.Staff;
import com.example.repositories.EnquiryRepository;
import com.example.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private EnquiryRepository enquiryRepository;

	@Autowired
	private StaffRepository staffRepository;

	@Override
	public List<Enquiry> getAllEnquiries() {
		return enquiryRepository.findAll();
	}

	@Override
	public Optional<Enquiry> getEnquiryById(Integer id) {
		return enquiryRepository.findById(id);
	}

	@Override
	public Enquiry saveEnquiry(Enquiry enquiry) {
		return enquiryRepository.save(enquiry);
	}

	@Override
	public void deleteEnquiry(Integer id) {
		enquiryRepository.deleteById(id);
	}

	@Override
	public Enquiry assignStaff(Integer enquiryId, AssignStaffRequest request) {
		Enquiry enquiry = enquiryRepository.findById(enquiryId)
				.orElseThrow(() -> new RuntimeException("Enquiry not found with ID: " + enquiryId));

		Staff staff = staffRepository.findById(request.getStaffId())
				.orElseThrow(() -> new RuntimeException("Staff not found with ID: " + request.getStaffId()));

		enquiry.setStaff(staff);

		if (request.getRemarks() != null && !request.getRemarks().isBlank()) {
			enquiry.setEnquirerQuery(enquiry.getEnquirerQuery() + " | Note: " + request.getRemarks());
		}

		return enquiryRepository.save(enquiry);
	}
}