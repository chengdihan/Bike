package com.example.bike.secrets;

public interface SecretStore {
  String getSecret(final String secretId);
}
