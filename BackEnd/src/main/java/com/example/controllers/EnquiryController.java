package com.example.controllers;

import com.example.dto.AssignStaffRequest;
import com.example.entities.ClosureReason;
import com.example.entities.Enquiry;
import com.example.repositories.ClosureReasonRepository;
import com.example.services.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

	@Autowired
	private EnquiryService enquiryService;

	@Autowired
	private ClosureReasonRepository closureReasonRepository;

	@GetMapping
	public ResponseEntity<List<Enquiry>> getAllEnquiries() {
		return ResponseEntity.ok(enquiryService.getAllEnquiries());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Enquiry> getEnquiryById(@PathVariable("id") Integer id) {
		return enquiryService.getEnquiryById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	public ResponseEntity<Enquiry> createEnquiry(@RequestBody Enquiry enquiry) {
		// enquiry_date is a system value ("today"), not something a staff
		// member fills in - stamp it here so the frontend never has to
		// send it (or show it) at all. This also means the server, not
		// the caller's browser clock, is the source of truth for it.
		enquiry.setEnquiryDate(LocalDate.now());

		// Safely resolve closure reason foreign key to prevent constraint crashes
		if (enquiry.getClosureReason() != null && enquiry.getClosureReason().getClosureReasonId() != null) {
			Integer reasonId = enquiry.getClosureReason().getClosureReasonId();
			ClosureReason managedReason = closureReasonRepository.findById(reasonId).orElse(null);
			enquiry.setClosureReason(managedReason);
		} else {
			enquiry.setClosureReason(null);
		}

		Enquiry savedEnquiry = enquiryService.saveEnquiry(enquiry);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedEnquiry);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Enquiry> updateEnquiry(@PathVariable("id") Integer id, @RequestBody Enquiry updatedEnquiry) {
		Optional<Enquiry> existingEnquiryOpt = enquiryService.getEnquiryById(id);

		if (existingEnquiryOpt.isPresent()) {
			Enquiry existingEnquiry = existingEnquiryOpt.get();

			existingEnquiry.setEnquirerName(updatedEnquiry.getEnquirerName());
			existingEnquiry.setEnquirerAddress(updatedEnquiry.getEnquirerAddress());
			existingEnquiry.setEnquirerMobile(updatedEnquiry.getEnquirerMobile());
			existingEnquiry.setEnquirerAlternateMobile(updatedEnquiry.getEnquirerAlternateMobile());
			existingEnquiry.setEnquirerEmailId(updatedEnquiry.getEnquirerEmailId());
			// enquiry_date is set once at creation and no longer collected
			// from the UI - only touch it here if a caller actually sends
			// one, so a normal edit can't silently null out the original
			// creation date.
			if (updatedEnquiry.getEnquiryDate() != null) {
				existingEnquiry.setEnquiryDate(updatedEnquiry.getEnquiryDate());
			}
			existingEnquiry.setEnquirerQuery(updatedEnquiry.getEnquirerQuery());
			existingEnquiry.setClosureReasonText(updatedEnquiry.getClosureReasonText());
			existingEnquiry.setEnquiryProcessedFlag(updatedEnquiry.getEnquiryProcessedFlag());
			existingEnquiry.setInquiryCounter(updatedEnquiry.getInquiryCounter());
			existingEnquiry.setFollowupDate(updatedEnquiry.getFollowupDate());
			existingEnquiry.setEnquirySource(updatedEnquiry.getEnquirySource());

			// Safely fetch managed ClosureReason to resolve foreign key exceptions
			if (updatedEnquiry.getClosureReason() != null
					&& updatedEnquiry.getClosureReason().getClosureReasonId() != null) {
				Integer reasonId = updatedEnquiry.getClosureReason().getClosureReasonId();
				ClosureReason managedReason = closureReasonRepository.findById(reasonId).orElse(null);
				existingEnquiry.setClosureReason(managedReason);
			} else {
				existingEnquiry.setClosureReason(null);
			}

			Enquiry savedEnquiry = enquiryService.saveEnquiry(existingEnquiry);
			return ResponseEntity.ok(savedEnquiry);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PutMapping("/{id}/assign-staff")
	public ResponseEntity<Enquiry> assignStaffToEnquiry(@PathVariable("id") Integer id,
			@RequestBody AssignStaffRequest request) {
		Enquiry updatedEnquiry = enquiryService.assignStaff(id, request);
		return ResponseEntity.ok(updatedEnquiry);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEnquiry(@PathVariable("id") Integer id) {
		if (enquiryService.getEnquiryById(id).isPresent()) {
			enquiryService.deleteEnquiry(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}