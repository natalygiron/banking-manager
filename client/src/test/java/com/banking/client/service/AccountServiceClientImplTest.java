package com.banking.client.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceClientImpl Tests")
class AccountServiceClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountServiceClientImpl accountServiceClient;

    private static final String ACCOUNT_SERVICE_URL = "http://localhost:8081/api/accounts/client";

    @BeforeEach
    void setUp() {
        // Any additional setup if needed
    }

    @Nested
    @DisplayName("hasAccounts Tests")
    class HasAccountsTests {

        @Test
        @DisplayName("Should return true when client has accounts")
        void shouldReturnTrueWhenClientHasAccounts() {
            // Given
            Long clientId = 1L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenReturn(responseEntity);

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isTrue();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should return false when client has no accounts")
        void shouldReturnFalseWhenClientHasNoAccounts() {
            // Given
            Long clientId = 2L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(false, HttpStatus.OK);
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenReturn(responseEntity);

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isFalse();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should return false when response body is null")
        void shouldReturnFalseWhenResponseBodyIsNull() {
            // Given
            Long clientId = 3L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenReturn(responseEntity);

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isFalse();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should return false when service returns 404")
        void shouldReturnFalseWhenServiceReturns404() {
            // Given
            Long clientId = 999L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenReturn(responseEntity);

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isFalse();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should return false when RestTemplate throws RestClientException")
        void shouldReturnFalseWhenRestTemplateThrowsRestClientException() {
            // Given
            Long clientId = 1L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenThrow(new RestClientException("Connection failed"));

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isFalse();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should return false when RestTemplate throws ResourceAccessException")
        void shouldReturnFalseWhenRestTemplateThrowsResourceAccessException() {
            // Given
            Long clientId = 1L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenThrow(new ResourceAccessException("Service unavailable"));

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isFalse();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should return false when RestTemplate throws generic RuntimeException")
        void shouldReturnFalseWhenRestTemplateThrowsRuntimeException() {
            // Given
            Long clientId = 1L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenThrow(new RuntimeException("Unexpected error"));

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isFalse();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should build correct URL with client ID")
        void shouldBuildCorrectUrlWithClientId() {
            // Given
            Long clientId = 12345L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(false, HttpStatus.OK);
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenReturn(responseEntity);

            // When
            accountServiceClient.hasAccounts(clientId);

            // Then
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should handle null clientId gracefully")
        void shouldHandleNullClientIdGracefully() {
            // Given
            Long clientId = null;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenThrow(new IllegalArgumentException("Client ID cannot be null"));

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isFalse();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }

        @Test
        @DisplayName("Should handle negative clientId")
        void shouldHandleNegativeClientId() {
            // Given
            Long clientId = -1L;
            String expectedUrl = ACCOUNT_SERVICE_URL + "/" + clientId + "/exists";
            ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            
            when(restTemplate.getForEntity(expectedUrl, Boolean.class))
                    .thenReturn(responseEntity);

            // When
            boolean result = accountServiceClient.hasAccounts(clientId);

            // Then
            assertThat(result).isFalse();
            verify(restTemplate).getForEntity(expectedUrl, Boolean.class);
        }
    }
}