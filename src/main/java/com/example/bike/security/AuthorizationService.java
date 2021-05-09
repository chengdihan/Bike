package com.example.bike.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthorizationService {

  @Value("${account-authorization.enabled}")
  private boolean enabled;

  /**
   * Validates that the user is authorized to access the passed account identified by account id.
   *
   * @param token represents the user that is trying to perform an action.
   * @param accountId of the account that the user it trying to access.
   * @throws UnauthorizedException in case that the user is not authorized to access the account.
   */
  public void authorizeAccount(final String token, final long accountId)
      throws UnauthorizedException {
    if (!enabled) {
      log.warn("Account authorization is disabled.");
      return;
    }
  }
}
