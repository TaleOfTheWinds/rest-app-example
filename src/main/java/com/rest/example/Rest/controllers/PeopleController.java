package com.rest.example.Rest.controllers;

import com.rest.example.Rest.dto.PersonDTO;
import com.rest.example.Rest.services.PeopleService;
import com.rest.example.Rest.util.exceptions.PersonErrorResponse;
import com.rest.example.Rest.util.exceptions.PersonNotFoundException;
import com.rest.example.Rest.util.exceptions.PersonValidationError;
import com.rest.example.Rest.validators.PersonDTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return peopleService.getPerson(id);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPerson(@RequestBody @Validated PersonDTO personDTO, BindingResult bindingResult) {
        personDTOValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors())
            throw new PersonValidationError(bindingResult.getAllErrors().stream().reduce((err, i) ->
                            new ObjectError("err", err.getDefaultMessage() + " ~~~ " + i.getDefaultMessage()))
                    .get().getDefaultMessage());
        peopleService.create(personDTO);
        return new ResponseEntity<>("Person created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updatePerson(@RequestBody @Validated PersonDTO personDTO, BindingResult bindingResult) {
        personDTOValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors())
            throw new PersonValidationError(bindingResult.getAllErrors().stream().reduce((err, i) ->
                            new ObjectError("err", err.getDefaultMessage() + " ~~~ " + i.getDefaultMessage()))
                    .get().getDefaultMessage());
        peopleService.update(personDTO);
        return new ResponseEntity<>("Person updated successfully", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
        return new ResponseEntity<>("Person deleted successfully", HttpStatus.ACCEPTED);
    }

    @ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(PersonValidationError e) {
        return new ResponseEntity<>(new PersonErrorResponse(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        return new ResponseEntity<>(new PersonErrorResponse("Person with this id wasn't found",
                LocalDateTime.now(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}
