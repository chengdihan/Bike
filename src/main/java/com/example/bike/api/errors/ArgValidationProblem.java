package com.example.bike.api.errors;

import java.util.Map;
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
public class ArgValidationProblem extends Problem {

  Map<String, String> validationErrors;

  static final class ArgValidationProblemBuilderImpl extends
      ArgValidationProblemBuilder<ArgValidationProblem, ArgValidationProblemBuilderImpl> {

  }
}
