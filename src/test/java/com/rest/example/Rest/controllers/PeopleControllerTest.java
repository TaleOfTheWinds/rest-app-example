package com.rest.example.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.example.rest.dto.PersonDTO;
import com.rest.example.rest.services.PeopleService;
import com.rest.example.rest.util.exceptions.PersonNotFoundException;
import com.rest.example.rest.validators.PersonDTOValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PeopleController.class)
public class PeopleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PeopleService peopleService;
    @MockBean
    private PersonDTOValidator validator;

    PersonDTO PERSON_1 = new PersonDTO(1, "Ivan", 20, "ivan@mail.ru");
    PersonDTO PERSON_2 = new PersonDTO(2, "Peter", 38, "peter@mail.ru");
    PersonDTO PERSON_3 = new PersonDTO(3, "Sonya", 23, "sonya@mail.ru");

    @Test
    public void getPeople_ok() throws Exception {
        List<PersonDTO> people = new ArrayList<>(Arrays.asList(PERSON_1, PERSON_2, PERSON_3));
        Mockito.when(peopleService.getPeople()).thenReturn(people);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/people")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Ivan")))
                .andExpect(jsonPath("$[2].name", is("Sonya")));

        verify(peopleService, times(1)).getPeople();
    }

    @Test
    public void getPersonById_ok() throws Exception {
        Mockito.when(peopleService.getPersonById(PERSON_2.getId())).thenReturn(PERSON_2);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/people/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Peter")));

        verify(peopleService, times(1)).getPersonById(PERSON_2.getId());
    }

    @Test
    public void getPersonById_notFound() throws Exception {
        Mockito.when(peopleService.getPersonById(10)).thenThrow(new PersonNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/people/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(peopleService, times(1)).getPersonById(10);
    }

    @Test
    public void createPerson_created() throws Exception {
        Mockito.when(peopleService.create(PERSON_1)).thenReturn(PERSON_1);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/people/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(PERSON_1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(PERSON_1.getName())));

        verify(peopleService, times(1)).create(PERSON_1);
    }

    @Test
    public void createPerson_badRequest_incorrectIdNameAgeEmail() throws Exception {
        PersonDTO personDTO = new PersonDTO(-1, "", -1, "abc");
        PersonDTO personDTO1 = new PersonDTO(0, "Ivan", 12, "");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/people/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem("Id should not be negative")))
                .andExpect(jsonPath("$.errors", hasItem("Name should not be empty")))
                .andExpect(jsonPath("$.errors", hasItem("Age should be greater than 0")))
                .andExpect(jsonPath("$.errors", hasItem("Email should be valid")));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/people/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(personDTO1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Email should not be empty")));

        verify(peopleService, times(0)).create(personDTO);
        verify(peopleService, times(0)).create(personDTO1);
    }

    @Test
    public void updatePerson_accepted() throws Exception {
        Mockito.when(peopleService.update(PERSON_1)).thenReturn(PERSON_1);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/people/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(PERSON_1)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(PERSON_1.getName())));

        verify(peopleService, times(1)).update(PERSON_1);
    }

    @Test
    public void updatePerson_badRequest_incorrectIdNameAgeEmail() throws Exception {
        PersonDTO personDTO = new PersonDTO(-1, "", -1, "abc");
        PersonDTO personDTO1 = new PersonDTO(0, "Ivan", 12, "");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/people/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem("Id should not be negative")))
                .andExpect(jsonPath("$.errors", hasItem("Name should not be empty")))
                .andExpect(jsonPath("$.errors", hasItem("Age should be greater than 0")))
                .andExpect(jsonPath("$.errors", hasItem("Email should be valid")));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/people/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(personDTO1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Email should not be empty")));

        verify(peopleService, times(0)).update(personDTO);
        verify(peopleService, times(0)).update(personDTO1);
    }

    @Test
    public void updatePerson_notFound() throws Exception {
        Mockito.when(peopleService.update(PERSON_1)).thenThrow(new PersonNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/people/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(PERSON_1)))
                .andExpect(status().isNotFound());

        verify(peopleService, times(1)).update(PERSON_1);
    }

    @Test
    public void deletePerson_accepted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/people/delete/1"))
                .andExpect(status().isAccepted());

        verify(peopleService, times(1)).delete(1);
    }

    @Test
    public void deletePerson_notFound() throws Exception {
        Mockito.doThrow(new PersonNotFoundException()).when(peopleService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/people/delete/1"))
                .andExpect(status().isNotFound());

        verify(peopleService, times(1)).delete(1);
    }
}
