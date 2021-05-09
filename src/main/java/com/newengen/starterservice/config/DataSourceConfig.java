package com.newengen.starterservice.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

// NOTE: Delete this config class if you are only using a single Jpa data source in your service.
// This is only here to disambiguate the primary data source from the secondary lothric data source.
@Configuration
@EnableJpaRepositories(
    basePackages = {
        "com.newengen.starterservice.dal.models",
        "com.newengen.starterservice.dal.repositories"
    }
)
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        final DataSource dataSource,
        final EntityManagerFactoryBuilder builder
    ) {
        return builder
            .dataSource(dataSource)
            .packages("com.newengen.starterservice.dal.models")
            .persistenceUnit("default")
            .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(
        final EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
