package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Recruiter;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {

    boolean existsByRecruiterName(String recruiterName);

}