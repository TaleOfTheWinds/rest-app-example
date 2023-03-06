package com.rest.example.Rest.controllers;

import com.rest.example.Rest.dto.PersonDTO;
import com.rest.example.Rest.services.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PeopleController {
    private final PeopleService peopleService;

    @GetMapping()
    public List<PersonDTO> getPeople() {
        return peopleService.getPeople();
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return peopleService.getPerson(id);
    }

    @PostMapping("/create")
    public void createPerson(@RequestBody PersonDTO personDTO) {
        peopleService.create(personDTO);
    }

    @PutMapping("/update")
    public void updatePerson(@RequestBody PersonDTO personDTO) {
        peopleService.update(personDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
    }
}
