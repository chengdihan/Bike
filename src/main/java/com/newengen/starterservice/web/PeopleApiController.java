package com.newengen.starterservice.web;

import com.newengen.starterservice.api.examples.people.AddPersonRequest;
import com.newengen.starterservice.api.examples.people.GetPeopleResponse;
import com.newengen.starterservice.api.examples.people.PeopleApi;
import com.newengen.starterservice.api.examples.people.Person;
import com.newengen.starterservice.api.examples.people.PersonListItem;
import com.newengen.starterservice.api.examples.people.UpdatePersonRequest;
import com.newengen.starterservice.dal.repositories.PeopleRepository;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PeopleApiController implements PeopleApi {

    private final PeopleRepository peopleRepository;

    @Override
    public ResponseEntity<GetPeopleResponse> getPeople() {
        var people = peopleRepository
            .findAll(Sort.by("lastName", "firstName"))
            .stream()
            .map(p -> PersonListItem.builder()
                .id(p.getId())
                .fullName(String.format("%s, %s", p.getLastName(), p.getFirstName()))
                .build()
            )
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            GetPeopleResponse.builder()
                .people(people)
                .build()
        );
    }

    @Override
    public ResponseEntity<Person> getPerson(@Valid long id) {
        var person = peopleRepository.findById(id)
            .map(p -> Person.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .build()
            );

        return ResponseEntity.of(person);
    }

    @Override
    public ResponseEntity<Person> addPerson(@Valid AddPersonRequest req) {
        var person = peopleRepository.saveAndFlush(
            com.newengen.starterservice.dal.models.Person.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build()
        );
        return ResponseEntity.ok(
            Person.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .build()
        );
    }

    @Override
    public ResponseEntity<Void> updatePerson(
        @Valid long id, @Valid UpdatePersonRequest req
    ) {
        var person = peopleRepository.findById(id);

        person.ifPresent(p ->
            peopleRepository.saveAndFlush(
                com.newengen.starterservice.dal.models.Person.builder()
                    .id(id)
                    .firstName(req.getFirstName())
                    .lastName(req.getLastName())
                    .build()
            )
        );

        if (person.isPresent()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> removePerson(@Valid long id) {
        if (peopleRepository.existsById(id)) {
            peopleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> getGreeting(@Valid long id) {
        var greeting = peopleRepository.findById(id)
            .map(p -> String.format("Hello, %s %s!", p.getFirstName(), p.getLastName()));

        return ResponseEntity.of(greeting);
    }
}
