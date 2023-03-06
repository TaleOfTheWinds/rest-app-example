package com.rest.example.Rest.validators;

import com.rest.example.Rest.dto.PersonDTO;
import com.rest.example.Rest.models.Person;
import com.rest.example.Rest.services.PeopleService;
import com.rest.example.Rest.util.exceptions.PersonValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.Optional;

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

    }
}
