package com.banking.client.service;

import com.banking.client.service.AccountServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Implementation of AccountServiceClient that communicates with the Account microservice.
 * Follows Dependency Inversion Principle by implementing the abstraction.
 */
@Slf4j
@Service
public class AccountServiceClientImpl implements AccountServiceClient {
    
    private final RestTemplate restTemplate;
    private final String accountServiceUrl;
    
    public AccountServiceClientImpl(RestTemplate restTemplate, 
                                    @Value("${services.account.url:http://localhost:8081}") String accountServiceUrl) {
        this.restTemplate = restTemplate;
        this.accountServiceUrl = accountServiceUrl;
    }
    
    @Override
    public boolean hasAccounts(Long clientId) {
        try {
            String url = accountServiceUrl + "/accounts/" + clientId;
            var response = restTemplate.getForEntity(url, List.class);
            var accounts = response.getBody();
            
            boolean hasActiveAccounts = response.getStatusCode().is2xxSuccessful() 
                    && accounts != null && !accounts.isEmpty();
            
            log.debug("Client {} has {} accounts", clientId, 
                    hasActiveAccounts && accounts != null ? accounts.size() : 0);
            
            return hasActiveAccounts;
            
        } catch (Exception e) {
            log.error("Error querying accounts-service for client {}: {}", clientId, e.getMessage());
            throw new IllegalStateException("Accounts service unavailable. Try again later.", e);
        }
    }
}