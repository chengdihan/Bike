package com.newengen.starterservice.api.examples.people;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name="PeopleExampleApi", description = "Example API that stores people in a SQL database and formats a greeting.")
@RequestMapping("/v1/examples/people")
public interface PeopleApi {

    @Operation(summary = "Get the list of people that have been added to the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "A list of people available in the system"),
    })
    @GetMapping
    ResponseEntity<GetPeopleResponse> getPeople();

    @Operation(summary = "Get a person by their id.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "The person with the specified id."),
        @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<Person> getPerson(@Valid @PathVariable("id") long id);

    @Operation(summary = "Add a new person to the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "The person with the assigned id.")
    })
    @PostMapping
    ResponseEntity<Person> addPerson(@Valid @RequestBody AddPersonRequest req);

    @Operation(summary = "Update a person's info")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = "text/plain",
                examples = @ExampleObject("Person not found")
            )
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<Void> updatePerson(@Valid @PathVariable("id") long id, @Valid @RequestBody UpdatePersonRequest req);

    @Operation(summary = "Remove a person from the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = "text/plain",
                examples = @ExampleObject("Person not found")
            )
        )
    })
    @DeleteMapping("{id}")
    ResponseEntity<Void> removePerson(@Valid @PathVariable("id") long id);

    @Operation(summary = "Get a nice greeting for a person.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "A nice greeting",
            content = @Content(
            mediaType = "text/plain",
            examples = @ExampleObject("Hello, John Doe!")
        )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = "text/plain",
                examples = @ExampleObject("Person not found")
            )
        )
    })
    @GetMapping("/{id}/greeting")
    ResponseEntity<String> getGreeting(@Valid@PathVariable("id") long id);
}
