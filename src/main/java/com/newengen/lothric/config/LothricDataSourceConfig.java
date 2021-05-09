package com.newengen.lothric.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "lothricEntityManagerFactory",
    transactionManagerRef = "lothricTransactionManager",
    basePackages = {
        "com.newengen.lothric.dal.models",
        "com.newengen.lothric.dal.repositories"
    }
)
public class LothricDataSourceConfig {

    @Bean
    @Qualifier("lothricDataSource")
    @ConfigurationProperties("lothric.datasource")
    public DataSourceProperties lothricDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource lothricDataSource(
        @Qualifier("lothricDataSource")
        final DataSourceProperties lothricDataSourceProperties
    ) {
        return lothricDataSourceProperties
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean lothricEntityManagerFactory(
        @Qualifier("lothricDataSource")
        final DataSource lothricDataSource,
        final EntityManagerFactoryBuilder builder
    ) {
        return builder
            .dataSource(lothricDataSource)
            .packages("com.newengen.lothric.dal.models")
            .persistenceUnit("lothric")
            .build();
    }

    @Bean
    @Qualifier("lothricTransactionManager")
    public PlatformTransactionManager lothricTransactionManager(
        @Qualifier("lothricEntityManagerFactory")
        final EntityManagerFactory lothricEntityManagerFactory
    ) {
        return new JpaTransactionManager(lothricEntityManagerFactory);
    }
}
