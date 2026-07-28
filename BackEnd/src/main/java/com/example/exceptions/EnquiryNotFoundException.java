package com.example.exceptions;

public class EnquiryNotFoundException extends ResourceNotFoundException {
	public EnquiryNotFoundException(String message) {
		super(message);
	}

	public EnquiryNotFoundException(Integer id) {
		super("Enquiry not found with ID: " + id);
	}
}