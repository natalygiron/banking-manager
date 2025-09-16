package com.banking.account.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.banking.account.dto.response.ClientResponse;
import lombok.AllArgsConstructor;

// Comunicación con microservicio de clientes
@Component
@AllArgsConstructor
public class ClientGateway {
    private final RestTemplate restTemplate;

    public boolean exists(Long clientId) {
        try {
            ResponseEntity<ClientResponse> response = restTemplate.getForEntity(
                    "http://localhost:8080/clientes/" + clientId,
                    ClientResponse.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }
}

