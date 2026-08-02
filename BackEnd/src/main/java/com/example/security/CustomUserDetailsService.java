package com.example.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entities.Staff;
import com.example.repositories.StaffRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;

    public CustomUserDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Staff staff = staffRepository
                .findByStaffUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid Username"));

        return User.builder()
                .username(staff.getStaffUsername())
                .password(staff.getStaffPassword())
                .roles(staff.getStaffRole())
                .build();
    }
}