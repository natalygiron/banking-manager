package com.banking.client;

import com.banking.client.dto.request.CreateClientRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.function.Consumer;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client register(CreateClientRequest createClientRequest) {

        Client client = Client.builder()
                .firstName(createClientRequest.getFirstName())
                .lastName(createClientRequest.getLastName())
                .email(createClientRequest.getEmail())
                .dni(createClientRequest.getDni())
                .build();

        if (isBlank(client.getFirstName()) || isBlank(client.getLastName()) ||
                isBlank(client.getDni()) || isBlank(client.getEmail())) {
            throw new ValidationException("All fields are required");
        }
        if (clientRepository.existsByDni(client.getDni())) {
            log.warn("Attempt to register client with existing DNI: {}", client.getDni());
            throw new IllegalArgumentException("Dni is already in use");
        }
        if (clientRepository.existsByEmail(client.getEmail())) {
            log.warn("Attempt to register client with existing email: {}", client.getEmail());
            throw new IllegalArgumentException("Email is already in use");
        }

        Client savedClient = clientRepository.save(client);
        log.info("Client registered successfully: {}", savedClient.getId());
        return savedClient;
    }

    @Transactional(readOnly = true)
    public Client get(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

    @Transactional(readOnly = true)
    public List<Client> list() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client updateClient(Long id, String firstName, String lastName, String email) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + id + " not found"));

        updateFieldIfPresent(client::setFirstName, firstName);
        updateFieldIfPresent(client::setLastName, lastName);

        if (isValidEmailUpdate(email, client.getEmail())) {
            if (clientRepository.existsByEmail(email)) {
                log.warn("Email '{}' is already in use by another client", email);
                throw new IllegalArgumentException("Email is already in use");
            }
            client.setEmail(email);
        }

        Client updated = clientRepository.save(client);
        log.info("Client updated successfully: {}", updated.getId());
        return updated;
    }

    private void updateFieldIfPresent(Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value.trim());
        }
    }

    private boolean isValidEmailUpdate(String newEmail, String currentEmail) {
        return newEmail != null && !newEmail.isBlank() && !newEmail.equalsIgnoreCase(currentEmail);
    }

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            log.warn("Attempt to delete client with id: {}", id);
            throw new IllegalArgumentException("Client not found");
        }

        if (clientHasAccounts(id)) {
            throw new ValidationException("Cannot delete client with active accounts");
        }

        clientRepository.deleteById(id);
        log.info("Client deleted: {}", id);
    }

    private boolean clientHasAccounts(Long clientId) {
        final String url = "http://localhost:8081/accounts/" + clientId;
        try {
            org.springframework.web.client.RestTemplate rt = new org.springframework.web.client.RestTemplate();
            var resp = rt.getForEntity(url, java.util.List.class);
            var body = resp.getBody();
            return resp.getStatusCode().is2xxSuccessful() && body != null && !body.isEmpty();
        } catch (Exception e) {
            log.error("Error querying accounts-service for client {}: {}", clientId, e.getMessage());
            throw new IllegalStateException("Accounts service unavailable. Try again later.");
        }
    }
}
