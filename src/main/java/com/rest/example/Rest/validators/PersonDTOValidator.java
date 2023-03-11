package com.rest.example.rest.validators;

import com.rest.example.rest.dto.PersonDTO;
import com.rest.example.rest.models.Person;
import com.rest.example.rest.services.PeopleService;
import com.rest.example.rest.util.exceptions.PersonValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PersonDTOValidator implements Validator {
    private final PeopleService peopleService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(PersonDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO person = (PersonDTO) target;
        Optional<Person> byEmail = peopleService.findByEmail(person.getEmail());
        if (byEmail.isPresent()) {
            if (byEmail.get().getId() != person.getId())
                errors.rejectValue("email", "", "Person with this email already exists");
        }

        if (errors.hasErrors()) {
            PersonValidationError error = new PersonValidationError();
            error.setErrors(errors.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList()));
            throw error;
        }
    }
}
