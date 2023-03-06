package com.rest.example.Rest.mappers;

import com.rest.example.Rest.dto.PersonDTO;
import com.rest.example.Rest.models.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    Person toPerson(PersonDTO personDTO);
    PersonDTO toDTO(Person person);
    List<Person> toPersonList(List<PersonDTO> list);
    List<PersonDTO> toDTOList(List<Person> list);
}
