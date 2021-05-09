package com.newengen.starterservice.api.examples.people;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPersonRequest {

    String firstName;
    String lastName;
}
