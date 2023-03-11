package com.rest.example.rest.controllers;

import com.rest.example.rest.dto.PersonDTO;
import com.rest.example.rest.services.PeopleService;
import com.rest.example.rest.util.exceptions.PersonErrorResponse;
import com.rest.example.rest.util.exceptions.PersonNotFoundException;
import com.rest.example.rest.util.exceptions.PersonValidationError;
import com.rest.example.rest.validators.PersonDTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonDTOValidator personDTOValidator;

    @GetMapping()
    public List<PersonDTO> getPeople() {
        return peopleService.getPeople();
    }

    @GetMapping("/{id}")
    public PersonDTO getPersonById(@PathVariable("id") int id) {
        return peopleService.getPersonById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<PersonDTO> createPerson(@RequestBody @Validated PersonDTO personDTO, BindingResult bindingResult) {
        personDTOValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            PersonValidationError error = new PersonValidationError();
            error.setErrors(bindingResult.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList()));
            throw error;
        }

        PersonDTO person = peopleService.create(personDTO);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody @Validated PersonDTO personDTO, BindingResult bindingResult) {
        personDTOValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            PersonValidationError error = new PersonValidationError();
            error.setErrors(bindingResult.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList()));
            throw error;
        }

        PersonDTO person = peopleService.update(personDTO);
        return new ResponseEntity<>(person, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(PersonValidationError e) {
        return new ResponseEntity<>(new PersonErrorResponse(e.getErrors(), LocalDateTime.now(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        return new ResponseEntity<>(new PersonErrorResponse(Collections.singletonList("Person with this id wasn't found"),
                LocalDateTime.now(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}
