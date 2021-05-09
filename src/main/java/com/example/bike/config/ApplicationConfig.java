package com.example.bike.config;

import com.example.bike.config.properties.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }
}
