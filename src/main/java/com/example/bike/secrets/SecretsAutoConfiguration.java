package com.example.bike.secrets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SecretsAutoConfiguration {

  @Bean(name = "secretStore")
  public SecretStore secretStore(final Environment env) {
    return new AwsSecretStore(env);
  }
}
