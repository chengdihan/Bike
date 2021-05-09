package com.newengen.starterservice.api.examples.lothric;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name="LothricExampleApi", description = "Example API that accesses entities in the Lothric SQL database")
@RequestMapping("/v1/examples/lothric")
public interface LothricExampleApi {

    @Operation(summary = "Gets Jove account details for a given account id.")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Account details."),
      @ApiResponse(
          responseCode = "404",
          description = "Account not found",
          content = @Content(
              mediaType = "text/plain",
              examples = @ExampleObject("Account not found")
          )
      )
    })
    @GetMapping("/accounts/{id}")
    ResponseEntity<JoveAccount> getAccount(
        @Valid
        @PathVariable("id")
        long id
    );
}
