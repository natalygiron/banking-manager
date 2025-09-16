package com.banking.client.service;

import com.banking.client.domain.Client;
import com.banking.client.dto.request.CreateClientRequest;
import com.banking.client.port.AccountsClient;
import java.util.List;
import java.util.function.Consumer;
import javax.validation.ValidationException;
import com.banking.client.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.apache.logging.log4j.util.Strings.isBlank;
@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountsClient accountsClient; // <<— DIP

    @Transactional
    public Client register(CreateClientRequest req) {
        Client client = Client.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .dni(req.getDni())
                .build();

        if (isBlank(client.getFirstName()) || isBlank(client.getLastName()) ||
                isBlank(client.getDni()) || isBlank(client.getEmail())) {
            throw new ValidationException("All fields are required");
        }
        if (clientRepository.existsByDni(client.getDni())) {
            throw new IllegalArgumentException("Dni is already in use");
        }
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        return clientRepository.save(client);
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

        updateIfPresent(client::setFirstName, firstName);
        updateIfPresent(client::setLastName, lastName);

        if (email != null && !email.isBlank() && !email.equalsIgnoreCase(client.getEmail())) {
            if (clientRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email is already in use");
            }
            client.setEmail(email);
        }
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client not found");
        }
        if (accountsClient.hasAccounts(id)) { // <<— ahora a través del puerto
            throw new ValidationException("Cannot delete client with active accounts");
        }
        clientRepository.deleteById(id);
    }

    private void updateIfPresent(Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) setter.accept(value.trim());
    }
}
