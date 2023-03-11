package com.rest.example.rest.services;

import com.rest.example.rest.dto.PersonDTO;
import com.rest.example.rest.models.Person;
import com.rest.example.rest.repositories.PeopleRepository;
import com.rest.example.rest.util.exceptions.PersonNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PeopleServiceTest {
    @Mock
    private PeopleRepository peopleRepository;

    @InjectMocks
    private PeopleService peopleService;

    Person person;
    Person person2;
    PersonDTO dto;

    @BeforeEach
    void setup() {
        person = new Person(1, "Ivan", 25, "mail@mail.ru", LocalDateTime.now(), LocalDateTime.now(), "ADMIN");
        person2 = new Person(2, "Peter", 20, "mail2@mail.ru", LocalDateTime.now(), LocalDateTime.now(), "ADMIN");
        dto = new PersonDTO(1, "Ivan", 25, "mail@mail.ru");
    }

    @Test
    public void getPeople_shouldReturnPeople() {
        List<Person> list = new ArrayList<>();
        list.add(person);
        list.add(person2);
        Mockito.when(peopleRepository.findAll()).thenReturn(list);

        List<PersonDTO> people = peopleService.getPeople();

        assertNotNull(people);
        assertEquals(people.size(), list.size());
        verify(peopleRepository, times(1)).findAll();
    }

    @Test
    public void getPersonById_shouldReturnPerson_whenExists() {
        Mockito.when(peopleRepository.findById(1)).thenReturn(Optional.of(person));

        PersonDTO personById = peopleService.getPersonById(1);

        assertEquals(person.getId(), personById.getId());
        assertEquals(person.getName(), personById.getName());
        assertEquals(person.getAge(), personById.getAge());
        assertEquals(person.getEmail(), personById.getEmail());
        verify(peopleRepository, times(1)).findById(1);
    }

    @Test
    public void getPersonById_shouldThrowException_whenDoesNotExist() {
        Mockito.when(peopleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> peopleService.getPersonById(1));
        verify(peopleRepository, times(1)).findById(1);
    }

    @Test
    public void create_shouldCreateCorrectly() {
        Mockito.when(peopleRepository.save(any(Person.class))).then(AdditionalAnswers.returnsFirstArg());

        PersonDTO dto1 = peopleService.create(dto);

        // Так как в сервисе всем дто ставится айди 0, а репозиторий должен сам менять его на новый, но не в тестах
        assertNotEquals(dto.getId(), dto1.getId());

        assertEquals(dto.getName(), dto1.getName());
        assertEquals(dto.getAge(), dto1.getAge());
        assertEquals(dto.getEmail(), dto1.getEmail());
        verify(peopleRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void update_shouldUpdateCorrectly_whenExists() {
        Mockito.when(peopleRepository.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(peopleRepository.save(any(Person.class))).then(AdditionalAnswers.returnsFirstArg());
        dto.setName("Maria");
        dto.setEmail(null);

        PersonDTO update = peopleService.update(dto);

        assertEquals(update.getName(), "Maria");
        assertNotNull(update.getEmail());
        assertEquals(update.getEmail(), person.getEmail());
        verify(peopleRepository, times(1)).findById(1);
        verify(peopleRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void update_shouldThrowException_whenDoesNotExist() {
        Mockito.when(peopleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> peopleService.update(dto));
        verify(peopleRepository, times(1)).findById(1);
    }

    @Test
    public void delete_shouldDeleteCorrectly_whenExists() {
        Mockito.when(peopleRepository.findById(1)).thenReturn(Optional.of(person));

        peopleService.delete(1);

        verify(peopleRepository, times(1)).findById(1);
        verify(peopleRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_shouldThrowException_whenDoesNotExist() {
        Mockito.when(peopleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> peopleService.delete(1));
        verify(peopleRepository, times(1)).findById(1);
        verify(peopleRepository, times(0)).deleteById(1);
    }

    @Test
    public void findByEmail_shouldReturnPersonCorrectly_whenExists() {
        Mockito.when(peopleRepository.findByEmail(anyString())).thenReturn(Optional.of(person));

        Optional<Person> abc = peopleService.findByEmail("abc");

        assertTrue(abc.isPresent());
        verify(peopleRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void findByEmail_shouldReturnNull_whenDoesNotExist() {
        Mockito.when(peopleRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<Person> abc = peopleService.findByEmail("abc");

        assertFalse(abc.isPresent());
        verify(peopleRepository, times(1)).findByEmail(anyString());
    }
}
