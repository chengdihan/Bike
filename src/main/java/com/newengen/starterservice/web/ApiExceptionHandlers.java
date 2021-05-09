package com.newengen.starterservice.web;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.newengen.starterservice.api.errors.ArgValidationProblem;
import com.newengen.starterservice.api.errors.ContentProblem;
import com.newengen.starterservice.api.errors.Problem;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class ApiExceptionHandlers extends ResponseEntityExceptionHandler {

    @Nonnull
    @Override
    protected ResponseEntity handleExceptionInternal(
        @Nonnull final Exception ex,
        final Object body,
        @Nonnull final HttpHeaders headers,
        @Nonnull final HttpStatus status,
        @Nonnull final WebRequest request
    ) {
        final var problem =
                Problem.builder()
                    .type("problem/internal")
                    .title(ex.getLocalizedMessage())
                    .instance(((HttpServletRequest)request).getRequestURI())
                    .build();

        return super.handleExceptionInternal(ex, problem, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Nonnull
    @Override
    protected ResponseEntity handleTypeMismatch(
        @Nonnull final TypeMismatchException ex,
        @Nonnull final HttpHeaders headers,
        @Nonnull final HttpStatus status,
        @Nonnull final WebRequest request
    ) {
        return ResponseEntity
            .status(status.value())
            .body(
                Problem.builder()
                    .type("problem/type-mismatch")
                    .title("Input type mismatch")
                    .detail(ex.getMessage())
                    .instance(((ServletWebRequest)request).getRequest().getRequestURI())
                    .build()
            );
    }

    @Nonnull
    @Override
    protected ResponseEntity handleHttpMessageNotReadable(
        @Nonnull final HttpMessageNotReadableException ex,
        @Nonnull final HttpHeaders headers,
        @Nonnull final HttpStatus status,
        @Nonnull final WebRequest request
    ) {
        if (ex.getMostSpecificCause() instanceof MismatchedInputException) {
            final var mie = (MismatchedInputException) (ex.getMostSpecificCause());
            final var fields = mie.getPath().stream().map(Reference::getFieldName).collect(Collectors.joining(", "));
            return ResponseEntity
                .badRequest()
                .body(
                    ContentProblem.builder()
                        .type("problem/message-not-readable")
                        .title("Input type mismatch")
                        .detail(ex.getMessage())
                        .instance(((ServletWebRequest)request).getRequest().getRequestURI())
                        .fields(fields)
                        .line(mie.getLocation().getLineNr())
                        .col(mie.getLocation().getColumnNr())
                        .build()
                );
        }

        return ResponseEntity
            .badRequest()
            .body(
                ContentProblem.builder()
                    .type("problem/message-not-readable")
                    .title("Content could not be read")
                    .instance(((ServletWebRequest)request).getRequest().getRequestURI())
                    .detail(ex.getMessage())
            );
    }

    @Nonnull
    @Override
    protected ResponseEntity handleMethodArgumentNotValid(
        @Nonnull final MethodArgumentNotValidException ex,
        @Nonnull final HttpHeaders headers,
        @Nonnull final HttpStatus status,
        @Nonnull final WebRequest request
    ) {
        final var validationErrors = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(FieldError::getField, e -> Optional.ofNullable(e.getDefaultMessage()).orElse("")));

        return ResponseEntity
            .badRequest()
            .body(
                ArgValidationProblem.builder()
                    .type("problem/argument-validation")
                    .title(ex.getMessage())
                    .detail(ex.getMessage())
                    .instance(((ServletWebRequest)request).getRequest().getRequestURI())
                    .validationErrors(validationErrors)
                    .instance(((HttpServletRequest)request).getRequestURI())
                    .build()
            );
    }
}
