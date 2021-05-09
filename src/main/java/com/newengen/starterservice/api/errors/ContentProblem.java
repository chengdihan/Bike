package com.newengen.starterservice.api.errors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ContentProblem extends Problem {

    String  fields;
    Integer line;
    Integer col;

    static final class ContentProblemBuilderImpl extends
        ContentProblemBuilder<ContentProblem, ContentProblemBuilderImpl> {}
}
