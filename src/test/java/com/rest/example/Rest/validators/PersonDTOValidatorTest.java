package com.rest.example.rest.validators;

import com.rest.example.rest.dto.PersonDTO;
import com.rest.example.rest.models.Person;
import com.rest.example.rest.services.PeopleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PersonDTOValidatorTest {
    @Mock
    private PeopleService peopleService;

    @InjectMocks
    private PersonDTOValidator validator;

    PersonDTO dto = new PersonDTO(1, "Ivan", 20, "mail@mail.ru");

    @Test
    public void shouldNotRejectValue_whenEmailDoesNotExist() {
        Mockito.when(peopleService.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Errors errors = new BeanPropertyBindingResult(dto, "dummy");

        validator.validate(dto, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void shouldRejectValue_whenEmailExist() {
        Person person = Person.builder().email("mail@mail.ru").build();
        Errors errors = new BeanPropertyBindingResult(dto, "dummy");
        Mockito.when(peopleService.findByEmail(Mockito.anyString())).thenReturn(Optional.of(person));

        validator.validate(dto, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void shouldNotRejectValue_whenIdsAreSame() {
        Person person = Person.builder().id(1).build();
        Errors errors = new BeanPropertyBindingResult(dto, "dummy");
        Mockito.when(peopleService.findByEmail(Mockito.anyString())).thenReturn(Optional.of(person));

        validator.validate(dto, errors);

        assertFalse(errors.hasErrors());
    }
}
