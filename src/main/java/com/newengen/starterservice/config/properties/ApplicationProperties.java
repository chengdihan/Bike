package com.newengen.starterservice.config.properties;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to this service.
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true)
public class ApplicationProperties {

    List<String> anonEndpoints;
}
