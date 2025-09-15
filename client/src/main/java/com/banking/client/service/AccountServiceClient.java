package com.banking.client.service;

/**
 * Interface for communicating with the Account service.
 * Follows Dependency Inversion Principle by abstracting external service dependencies.
 */
public interface AccountServiceClient {
    
    /**
     * Checks if a client has any active accounts
     * @param clientId the client ID to check
     * @return true if the client has active accounts, false otherwise
     */
    boolean hasAccounts(Long clientId);
}