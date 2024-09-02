package com.poc.cabio.service;

import com.poc.cabio.exception.UserException;
import com.poc.cabio.jwt.JwtService;
import com.poc.cabio.model.User;
import com.poc.cabio.repository.UserRepository;
import com.poc.cabio.request.LoginRequest;
import com.poc.cabio.request.SignupRequest;
import com.poc.cabio.response.LoginResponse;
import com.poc.cabio.response.SignupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ValidationService validationService;

    @Override
    public ResponseEntity<String> sendOtp(String email) throws UserException {
        try{
            int randomPin   =(int) (Math.random()*9000)+100000;
            String otp  = String.valueOf(randomPin);
            log.info(otp);
            String emailBody = "Your 6-digit OTP is:" + otp;
            sendEmail(email,"OTP",emailBody);
            return new ResponseEntity<>("OTP Successfully sent on the provided mail", HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>("Empty Value",HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("Wrong Email",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<SignupResponse> signup(SignupRequest signupRequest) throws UserException {
        try{
            User user1 = userRepository.findByEmail(signupRequest.getEmail().toLowerCase());
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if(validationService.nameValidation(signupRequest.getName())){
                log.info("Provided first name is not correct");
                SignupResponse signupResponse=new SignupResponse();
                signupResponse.setMessage("Provided first name is not correct");
                signupResponse.setSuccess(false);
                return new ResponseEntity<>(signupResponse,HttpStatus.OK);
            }
            if(validationService.mobileNumber(signupRequest.getMobileNumber())){
                log.info("Provided Contact Number is not correct");
                SignupResponse signupResponse = new SignupResponse();
                signupResponse.setMessage("Provided Contact Number is not correct");
                signupResponse.setSuccess(false);
                return new ResponseEntity<>(signupResponse,HttpStatus.OK);
            }
            if(validationService.emailValidation(signupRequest.getEmail())){
                log.info("Provided email is not correct");
                SignupResponse signupResponse=new SignupResponse();
                signupResponse.setMessage("Provided email is not correct");
                signupResponse.setSuccess(false);
                return new ResponseEntity<>(signupResponse,HttpStatus.OK);
            }
            if (user1 == null) {
                User u = new User();
                u.setName(signupRequest.getName());
                u.setEmail(signupRequest.getEmail().toLowerCase());
                u.setMobileNumber(signupRequest.getMobileNumber());
                u.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
                userRepository.save(u);
                SignupResponse signupResponse=new SignupResponse();
                signupResponse.setMessage("User Registered Successfully");
                signupResponse.setSuccess(true);
                log.info("User Registered successfully");
                return new ResponseEntity<>(signupResponse, HttpStatus.OK);
            } else {
                SignupResponse signupResponse=new SignupResponse();
                signupResponse.setMessage("User Already Present");
                signupResponse.setSuccess(false);
                log.info("User Already Present");
                return new ResponseEntity<>(signupResponse,HttpStatus.OK);
            }
        }catch (NullPointerException e){
            SignupResponse signupResponse=new SignupResponse();
            signupResponse.setMessage("Empty value");
            return new ResponseEntity<>(signupResponse,HttpStatus.OK);
        }
        catch (Exception e){
            log.error("Error in creating account",e);
            SignupResponse signupResponse=new SignupResponse();
            signupResponse.setMessage("Some error occurred in creating account");
            signupResponse.setSuccess(false);
            return new ResponseEntity<>(signupResponse,HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws UserException {

        try {
            User user = userRepository.findByEmail(loginRequest.getEmail().toLowerCase());
            if (user == null) {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setMessage("Email doesn't exist");
                loginResponse.setJwt(null);
                loginResponse.setSuccess(false);
                log.info("Email doesn't exist");
                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail().toLowerCase(), loginRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String jwt = (jwtService.generateToken(loginRequest.getEmail().toLowerCase()));
                userRepository.save(user);
                LoginResponse loginResponse=new LoginResponse();
                loginResponse.setMessage("Login Successfull!");
                loginResponse.setJwt(jwt);
                loginResponse.setSuccess(true);
                log.info("Login Successfull");
                return new ResponseEntity<>(loginResponse,HttpStatus.OK);
            }
            else {
                LoginResponse loginResponse=new LoginResponse();
                loginResponse.setMessage("Wrong Password");
                loginResponse.setJwt(null);
                loginResponse.setSuccess(false);
                log.info("Wrong Password");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }catch (NullPointerException e){
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setMessage("Email is null");
            return new ResponseEntity<>(loginResponse,HttpStatus.OK);
        }
    }
    private void sendEmail(String to,String subject,String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }
}
