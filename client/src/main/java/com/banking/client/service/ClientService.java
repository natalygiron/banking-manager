package com.banking.client.service;

import com.banking.client.dto.request.CreateClientRequest;
import com.banking.client.model.Client;
import com.banking.client.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.function.Consumer;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountServiceClient accountServiceClient;

    @Transactional
    public Client register(CreateClientRequest createClientRequest) {
        Client client = Client.builder()
                .firstName(createClientRequest.getFirstName())
                .lastName(createClientRequest.getLastName())
                .email(createClientRequest.getEmail())
                .dni(createClientRequest.getDni())
                .build();

        validateForCreation(client);

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

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            log.warn("Attempt to delete client with id: {}", id);
            throw new IllegalArgumentException("Client not found");
        }

        if (accountServiceClient.hasAccounts(id)) {
            throw new ValidationException("Cannot delete client with active accounts");
        }

        clientRepository.deleteById(id);
        log.info("Client deleted: {}", id);
    }

    private void validateForCreation(Client client) {
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
    }

    private void updateFieldIfPresent(Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value.trim());
        }
    }

    private boolean isValidEmailUpdate(String newEmail, String currentEmail) {
        return newEmail != null && !newEmail.isBlank() && !newEmail.equalsIgnoreCase(currentEmail);
    }
}