package com.newengen.starterservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class AuthorizationService {

    @Value("${account-service.url}")
    private String accountServiceUrl;

    @Value("${account-authorization.enabled}")
    private boolean enabled;

    /**
     * Validates that the user is authorized to access the passed account identified by account id.
     *
     * @param token     represents the user that is trying to perform an action.
     * @param accountId of the account that the user it trying to access.
     * @throws UnauthorizedException in case that the user is not authorized to access the account.
     */
    public void authorizeAccount(final String token, final long accountId) throws UnauthorizedException {
        if (!enabled) {
            log.warn("Account authorization is disabled.");
            return;
        }

        log.debug("Authorizing account {} via account service {}", accountId, accountServiceUrl);

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        final String accessEndpoint = accountServiceUrl + "/v1/accounts/" + accountId;
        final RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(accessEndpoint, HttpMethod.HEAD, new HttpEntity<Void>(headers), Void.class);
        } catch (final HttpStatusCodeException e) {
            throw new UnauthorizedException(accountId);
        }
    }
}
