package com.newengen.starterservice.api.errors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

// RFC-7807 Problem response.
// https://tools.ietf.org/html/rfc7807
@Getter
@ToString
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
public class Problem {

    String type;
    String title;
    String detail;
    String instance;
}

