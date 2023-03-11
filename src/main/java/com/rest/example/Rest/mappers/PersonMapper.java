package com.rest.example.rest.mappers;

import com.rest.example.rest.dto.PersonDTO;
import com.rest.example.rest.models.Person;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    Person toPerson(PersonDTO personDTO);
    PersonDTO toDTO(Person person);
    List<PersonDTO> toDTOList(List<Person> list);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PersonDTO personDTO, @MappingTarget Person person);
}
