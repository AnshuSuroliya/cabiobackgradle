package com.poc.cabio.controller;

import com.poc.cabio.exception.UserException;
import com.poc.cabio.exception.ValidationException;
import com.poc.cabio.request.LoginRequest;
import com.poc.cabio.request.OtpRequest;
import com.poc.cabio.request.SignupRequest;
import com.poc.cabio.response.LoginResponse;
import com.poc.cabio.response.SignupResponse;
import com.poc.cabio.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) throws UserException {
        return authService.signup(signupRequest);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse>login(@RequestBody LoginRequest loginRequest)throws UserException, ValidationException {
        return authService.login(loginRequest);
    }
    @PostMapping("/sendotp")
    public ResponseEntity<String> sentOtp(@RequestBody OtpRequest otpRequest) throws UserException {
        return authService.sendOtp(otpRequest.getEmail());
    }
}
