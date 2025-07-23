package com.ai.testai.controller;

import com.ai.testai.dto.PersonDTO;
import com.ai.testai.entity.Person;
import com.ai.testai.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/person")
    public ResponseEntity<PersonDTO> createTestPerson() {
        // Create entity
        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("User");
        person.setNationalCode("1234567890");
        person.setPhoneNumber("09123456789");
        person.setEmail("test@example.com");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        
        // Save to database
        Person savedPerson = personRepository.save(person);
        
        // Convert to DTO and return
        PersonDTO personDTO = modelMapper.map(savedPerson, PersonDTO.class);
        return ResponseEntity.ok(personDTO);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable Long id) {
        return personRepository.findById(id)
                .map(person -> {
                    // Convert entity to DTO
                    PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);
                    return ResponseEntity.ok(personDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
