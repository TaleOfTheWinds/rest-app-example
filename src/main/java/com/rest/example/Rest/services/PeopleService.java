package com.rest.example.Rest.services;

import com.rest.example.Rest.dto.PersonDTO;
import com.rest.example.Rest.mappers.PersonMapper;
import com.rest.example.Rest.models.Person;
import com.rest.example.Rest.repositories.PeopleRepository;
import com.rest.example.Rest.util.exceptions.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    public List<PersonDTO> getPeople() {
        List<Person> list = peopleRepository.findAll();
        return PersonMapper.INSTANCE.toDTOList(list);
    }

    public PersonDTO getPerson(int id) {
        Person person = peopleRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return PersonMapper.INSTANCE.toDTO(person);
    }

    @Transactional
    public void create(PersonDTO personDTO) {
        Person person = PersonMapper.INSTANCE.toPerson(personDTO);
        person.setCreated_at(LocalDateTime.now());
        person.setUpdated_at(LocalDateTime.now());
        person.setCreated_who("USER");

        peopleRepository.save(person);
    }

    @Transactional
    public void update(PersonDTO personDTO) {
        Person person = peopleRepository.findById(personDTO.getId()).orElseThrow(PersonNotFoundException::new);
        PersonMapper.INSTANCE.updateEntity(personDTO, person);
        person.setUpdated_at(LocalDateTime.now());
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        Person person = peopleRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        peopleRepository.deleteById(id);
    }

    public Optional<Person> findByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }
}
