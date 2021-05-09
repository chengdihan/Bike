package com.newengen.starterservice.web;

import com.newengen.lothric.dal.repositories.JoveAccountRepository;
import com.newengen.starterservice.api.examples.lothric.LothricExampleApi;
import com.newengen.starterservice.api.examples.lothric.JoveAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LothricExampleApiController implements LothricExampleApi {

    // Lombok @RequiredArgsConstructor annotation generates a constructor parameter for
    // all uninitialized private fields. De-lombok the @RequiredArgsConstructor annotation
    // if you need to apply annotations to the constructor parameters.

    // Spring will automatically inject constructor dependencies that have been annotated with
    // @Component or @Service
    private final JoveAccountRepository accountRepository;

    @Override
    public ResponseEntity<JoveAccount> getAccount(final long id) {
        final var account = accountRepository
            .findById(id)
            .map(a -> JoveAccount.builder()
                .id(a.getId())
                .name(a.getName())
                .updated_at(a.getUpdated_at())
                .build()
            );

        return ResponseEntity.of(account);
    }
}
