package com.example.repositories;

import com.example.entities.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer> {
    List<Enquiry> findByEnquirerEmailId(String enquirerEmailId);
}