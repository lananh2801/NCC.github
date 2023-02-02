package com.demo.opentalk.service;

import com.demo.opentalk.config.LoginRequest;
import com.demo.opentalk.config.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> registerUser(SignUpRequest signUpRequest);
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
}