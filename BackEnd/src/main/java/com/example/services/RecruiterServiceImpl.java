package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.RecruiterRequest;
import com.example.dto.RecruiterResponse;
import com.example.entities.Recruiter;
import com.example.exceptions.RecruiterNotFoundException;
import com.example.repositories.RecruiterRepository;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public RecruiterResponse addRecruiter(RecruiterRequest recruiterRequest) {

        Recruiter recruiter = new Recruiter();

        recruiter.setPhotoUrl(recruiterRequest.getPhotoUrl());
        recruiter.setRecruiterName(recruiterRequest.getRecruiterName());
        recruiter.setDescription(recruiterRequest.getDescription());

        Recruiter savedRecruiter = recruiterRepository.save(recruiter);

        return mapToResponse(savedRecruiter);
    }

    @Override
    public RecruiterResponse getRecruiterById(Integer recruiterId) {

        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() ->
                        new RecruiterNotFoundException(
                                "Recruiter not found with id : "
                                        + recruiterId));

        return mapToResponse(recruiter);
    }

    @Override
    public List<RecruiterResponse> getAllRecruiters() {

        List<Recruiter> recruiters = recruiterRepository.findAll();

        List<RecruiterResponse> responseList = new ArrayList<>();

        for (Recruiter recruiter : recruiters) {
            responseList.add(mapToResponse(recruiter));
        }

        return responseList;
    }

    @Override
    public RecruiterResponse updateRecruiter(Integer recruiterId,
                                             RecruiterRequest recruiterRequest) {

        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() ->
                        new RecruiterNotFoundException(
                                "Recruiter not found with id : "
                                        + recruiterId));

        recruiter.setPhotoUrl(recruiterRequest.getPhotoUrl());
        recruiter.setRecruiterName(recruiterRequest.getRecruiterName());
        recruiter.setDescription(recruiterRequest.getDescription());

        Recruiter updatedRecruiter =
                recruiterRepository.save(recruiter);

        return mapToResponse(updatedRecruiter);
    }

    @Override
    public void deleteRecruiter(Integer recruiterId) {

        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() ->
                        new RecruiterNotFoundException(
                                "Recruiter not found with id : "
                                        + recruiterId));

        recruiterRepository.delete(recruiter);
    }

    private RecruiterResponse mapToResponse(Recruiter recruiter) {

        RecruiterResponse response = new RecruiterResponse();

        response.setRecruiterId(recruiter.getRecruiterId());
        response.setPhotoUrl(recruiter.getPhotoUrl());
        response.setRecruiterName(recruiter.getRecruiterName());
        response.setDescription(recruiter.getDescription());

        return response;
    }

}