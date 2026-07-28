package com.example.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.ChangePasswordRequest;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.entities.Staff;
import com.example.repositories.StaffRepository;
import com.example.security.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    private final StaffRepository staffRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(StaffRepository staffRepository,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {

        this.staffRepository = staffRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        // Authenticate username & password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getStaffUsername(),
                        loginRequest.getStaffPassword()));

        // Fetch staff details
        Staff staff = staffRepository
                .findByStaffUsername(loginRequest.getStaffUsername())
                .orElseThrow(() ->
                        new RuntimeException("Invalid Username or Password"));

        // Generate JWT Token
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(staff.getStaffUsername())
                        .password(staff.getStaffPassword())
                        .roles(staff.getStaffRole())
                        .build());

        // Prepare Response
        LoginResponse response = new LoginResponse();

        response.setAccessToken(token);
        response.setStaffId(staff.getStaffId());
        response.setStaffName(staff.getStaffName());
        response.setStaffUsername(staff.getStaffUsername());
        response.setStaffEmail(staff.getStaffEmail());
        response.setStaffRole(staff.getStaffRole());

        return response;
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        Staff staff = staffRepository
                .findByStaffUsername(request.getStaffUsername())
                .orElseThrow(() ->
                        new RuntimeException("Staff not found"));

        // Verify Current Password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                staff.getStaffPassword())) {

            throw new RuntimeException("Current Password is incorrect.");
        }

        // Verify New Password
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new RuntimeException("New Password and Confirm Password do not match.");
        }

        // Encode Password
        staff.setStaffPassword(
                passwordEncoder.encode(request.getNewPassword()));

        staffRepository.save(staff);
    }

    @Override
    public void logout() {

        // JWT is stateless.
        // Nothing to do on the server.
        // Frontend simply removes the token.

    }
}