package com.rest.example.rest.util.exceptions;

public class PersonValidationError extends RuntimeException{
    public PersonValidationError(String message) {
        super(message);
    }
}
