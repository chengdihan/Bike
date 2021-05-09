package com.example.bike.secrets;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.example.bike.utils.AwsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

/**
 * Provides access to secrets stored in AWS Secrets Manager service.
 */
@Slf4j
public class AwsSecretStore implements SecretStore {

  private final Environment env;

  public AwsSecretStore(final Environment env) {
    this.env = env;
  }

  public String getSecret(final String secretId) {
    Regions clientRegion = Regions.US_EAST_1;

    final AWSSecretsManager client = AWSSecretsManagerClientBuilder
        .standard()
        .withRegion(clientRegion)
        .build();

    final AWSCredentialsProviderChain providerChain = AwsUtil.awsCredentialsProviderChain();

    final GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
        .withSecretId(secretId)
        .withRequestCredentialsProvider(providerChain);

    final GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);

    log.info("Successfully retrieved secret: {}", secretId);

    return getSecretValueResult.getSecretString();
  }
}
