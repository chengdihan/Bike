package com.example.bike.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import org.springframework.core.env.Environment;

/**
 * @author dcheng
 */
public class AwsUtil {

  private static Environment env = null;

  public AwsUtil(Environment env) {
    this.env = env;
  }

  public static AWSCredentialsProviderChain awsCredentialsProviderChain() {
    return new AWSCredentialsProviderChain(
        new EC2ContainerCredentialsProviderWrapper(),
        // this is for aws nodes to use instance profile/role to auth
        new AWSCredentialsProvider() {
          @Override
          public AWSCredentials getCredentials() {
            return new AWSCredentials() {
              @Override
              public String getAWSAccessKeyId() {
                return env.getProperty("aws.credentials.key");
              }

              @Override
              public String getAWSSecretKey() {
                return env.getProperty("aws.credentials.secret");
              }
            };
          }

          @Override
          public void refresh() {

          }
        },
        new ProfileCredentialsProvider("profile3")
    );
  }
}
