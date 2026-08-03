package com.example.services;

import java.security.GeneralSecurityException;
import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.ChangePasswordRequest;
import com.example.dto.GoogleLoginRequest;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.entities.Staff;
import com.example.repositories.StaffRepository;
import com.example.security.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class AuthServiceImpl implements AuthService {

    private final StaffRepository staffRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public AuthServiceImpl(StaffRepository staffRepository,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           @Value("${google.client.id}") String googleClientId) {

        this.staffRepository = staffRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

        // Built once and reused - verifies the token's signature against
        // Google's public keys and checks it was issued for OUR client id,
        // so a token meant for some other app can't be replayed here.
        this.googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
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
    public LoginResponse loginWithGoogle(GoogleLoginRequest request) {

        GoogleIdToken idToken;

        try {
            idToken = googleIdTokenVerifier.verify(request.getIdToken());
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            throw new RuntimeException("Could not verify Google token.");
        }

        if (idToken == null) {
            throw new RuntimeException("Invalid or expired Google token.");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new RuntimeException("Google account email is not verified.");
        }

        String email = payload.getEmail();

        // Only staff already added to the system (via Staff management)
        // may sign in with Google - we match by email, never auto-create
        // a staff/admin account just because someone has a Google login.
        Staff staff = staffRepository
                .findByStaffEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No staff account found for " + email
                                        + ". Ask an administrator to add you first."));

        // Generate the exact same kind of JWT normal login produces -
        // JwtAuthenticationFilter and CustomUserDetailsService don't need
        // to know or care that this login started at Google.
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(staff.getStaffUsername())
                        .password(staff.getStaffPassword() == null ? "" : staff.getStaffPassword())
                        .roles(staff.getStaffRole())
                        .build());

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