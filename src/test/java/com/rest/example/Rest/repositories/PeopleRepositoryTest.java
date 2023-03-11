package com.rest.example.rest.repositories;

import com.rest.example.rest.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PeopleRepositoryTest {

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        Person person = new Person(1, "Ivan", 25, "mail@mail.ru", LocalDateTime.now(), LocalDateTime.now(), "ADMIN");
        Person person2 = new Person(2, "Peter", 20, "mail2@mail.ru", LocalDateTime.now(), LocalDateTime.now(), "ADMIN");
        entityManager.merge(person);
        entityManager.merge(person2);
    }

    @Test
    public void findById_shouldFindPerson_whenExists() {
        Optional<Person> person = peopleRepository.findById(1);

        assertTrue(person.isPresent());
    }

    @Test
    public void findById_shouldReturnOptionalNull_whenDoesNotExist() {
        Optional<Person> person = peopleRepository.findById(3);

        assertFalse(person.isPresent());
    }

    @Test
    public void findAll_shouldReturnAllPeople() {
        List<Person> all = peopleRepository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    public void findByEmail_shouldFindPerson_whenExists() {
        Optional<Person> person = peopleRepository.findByEmail("mail@mail.ru");

        assertTrue(person.isPresent());
        assertEquals("Ivan", person.get().getName());
    }

    @Test
    public void save_shouldCreatePersonCorrectly() {
        peopleRepository.save(new Person(3, "Sonya", 30, "mail35@mail.ru", LocalDateTime.now(), LocalDateTime.now(), "USER"));
        Optional<Person> person = peopleRepository.findById(3);

        assertTrue(person.isPresent());
        assertEquals("Sonya", person.get().getName());
    }

    @Test
    public void save_shouldUpdatePersonCorrectly() {
        Person person1 = peopleRepository.findById(1).get();
        person1.setName("Maria");
        person1.setEmail("test@test.ru");
        peopleRepository.save(person1);
        Optional<Person> person2 = peopleRepository.findByEmail("test@test.ru");

        assertTrue(person2.isPresent());
        assertEquals("Maria", person2.get().getName());
    }

    @Test
    public void delete_shouldDeletePersonCorrectly() {
        peopleRepository.deleteById(1);
        List<Person> all = peopleRepository.findAll();

        assertEquals(1, all.size());
    }
}
