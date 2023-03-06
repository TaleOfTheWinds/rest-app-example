package com.rest.example.Rest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PersonDTO {
    @Min(value = 0, message = "Id should be greater than 0")
    private int id;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @Min(value = 0, message = "Age should be greater than 0")
    private int age;
    @Email(message = "Email should be valid")
    private String email;
}
