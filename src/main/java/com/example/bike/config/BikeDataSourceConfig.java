package com.example.bike.config;

import com.example.bike.secrets.SecretStore;
import javax.naming.ConfigurationException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@Profile({"staging", "prod"})
public class BikeDataSourceConfig extends BaseSecretPropertyConfig {

  private static final String URI_PREFIX = "jdbc:mysql://";

  @Value("${spring.datasource.driver-class-name}")
  private String DRIVER_CLASS_NAME;

  @Autowired
  public BikeDataSourceConfig(Environment env,
      SecretStore secretStore) {
    super(env, secretStore);
  }

  @Bean
  @Primary
  public DataSource dataSource() throws ConfigurationException {
    final AwsSecretProperties awsSecretProperties = getCredentials("bike.database.credentials");
    final String url = URI_PREFIX + awsSecretProperties.host + "/"
        + awsSecretProperties.dbname
        + "?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull";
    
    DataSource ds = DataSourceBuilder.create()
        .driverClassName(DRIVER_CLASS_NAME)
        .username(awsSecretProperties.username).password(awsSecretProperties.password).url(url)
        .build();

    log.info("Data Source: \n{}", ds.toString());

    return ds;
  }
}
