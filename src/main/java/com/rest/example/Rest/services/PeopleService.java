package com.rest.example.rest.services;

import com.rest.example.rest.dto.PersonDTO;
import com.rest.example.rest.mappers.PersonMapper;
import com.rest.example.rest.models.Person;
import com.rest.example.rest.repositories.PeopleRepository;
import com.rest.example.rest.util.exceptions.PersonNotFoundException;
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

    public PersonDTO getPersonById(int id) {
        Person person = peopleRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return PersonMapper.INSTANCE.toDTO(person);
    }

    @Transactional
    public PersonDTO create(PersonDTO personDTO) {
        Person person = PersonMapper.INSTANCE.toPerson(personDTO);
        person.setId(0);
        person.setCreated_at(LocalDateTime.now());
        person.setUpdated_at(LocalDateTime.now());
        person.setCreated_who("USER");

        return PersonMapper.INSTANCE.toDTO(peopleRepository.save(person));
    }

    @Transactional
    public PersonDTO update(PersonDTO personDTO) {
        Person person = peopleRepository.findById(personDTO.getId()).orElseThrow(PersonNotFoundException::new);
        PersonMapper.INSTANCE.updateEntity(personDTO, person);
        person.setUpdated_at(LocalDateTime.now());

        return PersonMapper.INSTANCE.toDTO(peopleRepository.save(person));
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
