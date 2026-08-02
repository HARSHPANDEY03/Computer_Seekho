package com.example.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.ChangePasswordRequest;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;	

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Staff Login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        LoginResponse response = authService.login(loginRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * Change Password
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        authService.changePassword(request);

        return ResponseEntity.ok("Password changed successfully.");
    }

    /**
     * Logout
     * (JWT logout is usually handled on the client side or by token blacklisting.)
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        authService.logout();

        return ResponseEntity.ok("Logged out successfully.");
    }

}