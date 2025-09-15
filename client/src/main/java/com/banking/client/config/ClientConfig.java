package com.banking.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for Client microservice.
 * Provides bean definitions following Spring best practices.
 */
@Configuration
public class ClientConfig {
    
    /**
     * Creates a RestTemplate bean for HTTP communications
     * @return configured RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}