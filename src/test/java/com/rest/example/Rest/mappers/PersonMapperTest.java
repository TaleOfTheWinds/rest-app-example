package com.rest.example.rest.mappers;

import com.rest.example.rest.dto.PersonDTO;
import com.rest.example.rest.models.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonMapperTest {

    @Test
    public void shouldMapDtoCorrectly() {
        PersonDTO dto = new PersonDTO(1, "test", 2, "test@mail.ru");

        Person person = PersonMapper.INSTANCE.toPerson(dto);

        assertEquals(person.getId(), 1);
        assertEquals(person.getName(), "test");
        assertEquals(person.getAge(), 2);
        assertEquals(person.getEmail(), "test@mail.ru");
    }

    @Test
    public void shouldMapPersonCorrectly() {
        Person person = new Person(1, "test", 2, "test@mail.ru", LocalDateTime.now(), LocalDateTime.now(), "ADMIN");

        PersonDTO dto = PersonMapper.INSTANCE.toDTO(person);

        assertEquals(person.getId(), dto.getId());
        assertEquals(person.getName(), dto.getName());
        assertEquals(person.getAge(), dto.getAge());
        assertEquals(person.getEmail(), dto.getEmail());
    }

    @Test
    public void shouldMapPersonListCorrectly() {
        List<Person> list = new ArrayList<>();
        list.add(new Person(1, "a", 2, "a@a", LocalDateTime.now(), LocalDateTime.now(), "ADMIN"));
        list.add(new Person(2, "b", 5, "b@b", LocalDateTime.now(), LocalDateTime.now(), "ADMIN"));
        list.add(new Person(3, "c", 10, "c@c", LocalDateTime.now(), LocalDateTime.now(), "ADMIN"));

        List<PersonDTO> personDTOS = PersonMapper.INSTANCE.toDTOList(list);

        assertEquals(list.size(), personDTOS.size());
        assertEquals(list.get(1).getId(), personDTOS.get(1).getId());
        assertEquals(list.get(1).getAge(), personDTOS.get(1).getAge());
        assertEquals(list.get(1).getName(), personDTOS.get(1).getName());
        assertEquals(list.get(1).getEmail(), personDTOS.get(1).getEmail());
    }

    @Test
    public void shouldUpdateEntityCorrectly() {
        Person person = new Person(1, "Ivan", 25, "mail@mail.ru", LocalDateTime.now(), LocalDateTime.now(), "ADMIN");
        PersonDTO dto = PersonDTO.builder().id(12).age(40).name("Peter").build();

        PersonMapper.INSTANCE.updateEntity(dto, person);

        assertEquals(person.getName(), dto.getName());
        assertEquals(person.getAge(), dto.getAge());
        assertEquals(person.getId(), dto.getId());
        assertNotEquals(person.getEmail(), dto.getEmail());
    }
}
