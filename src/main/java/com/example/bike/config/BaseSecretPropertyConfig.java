package com.example.bike.config;

import com.example.bike.secrets.SecretStore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import javax.naming.ConfigurationException;
import org.springframework.core.env.Environment;

public class BaseSecretPropertyConfig {

  private final Environment env;
  private final SecretStore secretStore;

  public BaseSecretPropertyConfig(final Environment env, final SecretStore secretStore) {
    this.env = env;
    this.secretStore = secretStore;
  }

  public AwsSecretProperties getCredentials(String credentialKey) throws ConfigurationException {
    final String secret = Optional.ofNullable(env.getProperty(credentialKey))
        .map(secretStore::getSecret).orElseThrow(() -> new ConfigurationException(
            "Missing or invalid configuration value for '" + credentialKey + "'"));

    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(secret, AwsSecretProperties.class);
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class AwsSecretProperties {

    @JsonProperty
    String username;
    @JsonProperty
    String password;
    @JsonProperty
    String host;
    @JsonProperty
    String port;
    @JsonProperty
    String dbname;
    @JsonProperty
    String allowedOrigins;
  }
}
