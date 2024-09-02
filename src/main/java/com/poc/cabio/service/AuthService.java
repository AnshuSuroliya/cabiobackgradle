package com.poc.cabio.service;

import com.poc.cabio.exception.UserException;
import com.poc.cabio.exception.ValidationException;
import com.poc.cabio.request.LoginRequest;
import com.poc.cabio.request.SignupRequest;
import com.poc.cabio.response.LoginResponse;
import com.poc.cabio.response.SignupResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> sendOtp(String email)throws UserException;
    ResponseEntity<SignupResponse> signup(SignupRequest signupRequest) throws UserException;

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws UserException, ValidationException;
}
