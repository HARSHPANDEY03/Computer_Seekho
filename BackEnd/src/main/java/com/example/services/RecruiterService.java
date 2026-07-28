package com.example.services;

import java.util.List;

import com.example.dto.RecruiterRequest;
import com.example.dto.RecruiterResponse;

public interface RecruiterService {

    RecruiterResponse addRecruiter(RecruiterRequest recruiterRequest);

    RecruiterResponse getRecruiterById(Integer recruiterId);

    List<RecruiterResponse> getAllRecruiters();

    RecruiterResponse updateRecruiter(Integer recruiterId,
                                      RecruiterRequest recruiterRequest);

    void deleteRecruiter(Integer recruiterId);

}