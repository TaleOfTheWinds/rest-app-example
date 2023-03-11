package com.rest.example.rest.util.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PersonErrorResponse {
    private List<String> errors;
    private LocalDateTime timestamp;
    private HttpStatus statusCode;
}
