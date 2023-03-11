package com.rest.example.rest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "people")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0, message = "Id should be greater than 0")
    private int id;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @Min(value = 0, message = "Age should be greater than 0")
    private int age;
    @Email(message = "Email should be valid")
    private String email;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private String created_who;
}
