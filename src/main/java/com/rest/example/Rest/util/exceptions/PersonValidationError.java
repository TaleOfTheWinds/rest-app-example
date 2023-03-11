package com.rest.example.rest.util.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonValidationError extends RuntimeException{
    private List<String> errors;
}
