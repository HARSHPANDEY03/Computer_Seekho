// src/main/java/com/example/services/EnquiryServiceImpl.java
package com.example.services;

import com.example.dto.AssignStaffRequest;
import com.example.entities.Enquiry;
import com.example.entities.Staff;
import com.example.repositories.EnquiryRepository;
import com.example.repositories.FollowupRepository;
import com.example.repositories.StaffRepository;
import com.example.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private EnquiryRepository enquiryRepository;

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private FollowupRepository followupRepository;

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
	@Transactional
	public void deleteEnquiry(Integer id) {
		Enquiry enquiry = enquiryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Enquiry not found with ID: " + id));

		// A registered student is a real record, not disposable history —
		// keep it, just detach it from the enquiry being deleted.
		studentRepository.findByEnquiryEnquiryId(id).ifPresent(student -> {
			student.setEnquiry(null);
			studentRepository.save(student);
		});

		// Follow-up history only makes sense attached to its enquiry —
		// once the enquiry is gone there's nothing left for it to belong
		// to, so it goes with it. Without this, MySQL rejects the delete
		// below with a raw foreign-key error.
		followupRepository.deleteByEnquiryEnquiryId(id);

		enquiryRepository.delete(enquiry);
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