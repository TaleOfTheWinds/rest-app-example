package com.rest.example.Rest.services;

import com.rest.example.Rest.dto.PersonDTO;
import com.rest.example.Rest.mappers.PersonMapper;
import com.rest.example.Rest.models.Person;
import com.rest.example.Rest.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Person person = peopleRepository.findById(id).get();
        return PersonMapper.INSTANCE.toDTO(person);
    }
}
