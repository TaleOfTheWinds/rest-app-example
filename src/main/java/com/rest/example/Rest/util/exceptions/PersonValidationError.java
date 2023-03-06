package com.rest.example.Rest.util.exceptions;

public class PersonValidationError extends RuntimeException{
    public PersonValidationError(String message) {
        super(message);
    }
}
