package com.poc.cabio.service;

import com.poc.cabio.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService{
    @Value("${validation.signup.name.regEx}")
    private String validName;

    @Value("${validation.phoneNumber.regEx1}")
    private String validNumber;

    @Value("${validation.email.regEx2}")
    private String validEmail;
    @Override
    public Boolean nameValidation(String name) throws ValidationException {
        return name.matches(validName);
    }

    @Override
    public Boolean emailValidation(String email) throws ValidationException {
        return email.matches(validEmail);
    }

    @Override
    public Boolean mobileNumber(String number) throws ValidationException {
        return number.matches(validNumber);
    }

}
