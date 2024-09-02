package com.poc.cabio.service;

import com.poc.cabio.exception.ValidationException;

public interface ValidationService {
    public Boolean nameValidation(String name) throws ValidationException;
    public Boolean emailValidation(String email) throws ValidationException;

    public Boolean mobileNumber(String number)throws ValidationException;
}
