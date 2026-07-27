package com.example.services;

import com.example.entities.Enquiry;
import com.example.repositories.EnquiryRepository;
import com.example.services.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;

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
}