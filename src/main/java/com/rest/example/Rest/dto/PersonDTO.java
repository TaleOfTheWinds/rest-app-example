package com.rest.example.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
