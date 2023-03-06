package com.rest.example.Rest.util.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class PersonErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private HttpStatus statusCode;
}
