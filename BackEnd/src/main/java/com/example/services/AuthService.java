package com.example.services;

import com.example.dto.ChangePasswordRequest;
import com.example.dto.GoogleLoginRequest;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;

public interface AuthService {

    /**
     * Authenticate staff and generate JWT token.
     *
     * @param loginRequest Login credentials
     * @return LoginResponse containing JWT token and staff details
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * Authenticate staff using a Google ID token and generate the same
     * JWT that normal login produces. The email inside the verified
     * Google token must already belong to an existing Staff record.
     *
     * @param request GoogleLoginRequest containing the Google ID token
     * @return LoginResponse containing JWT token and staff details
     */
    LoginResponse loginWithGoogle(GoogleLoginRequest request);

    /**
     * Change the password of a staff member.
     *
     * @param request ChangePasswordRequest containing old and new passwords
     */
    void changePassword(ChangePasswordRequest request);

    /**
     * Logout the current user.
     * For JWT, this can be implemented using token blacklisting if required.
     */
    void logout();

}