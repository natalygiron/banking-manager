package com.banking.client;

import com.banking.client.dto.request.CreateClientRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;

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

        if(isBlank(client.getFirstName()) || isBlank(client.getLastName()) || isBlank(client.getDni()) || isBlank(client.getEmail())){
            throw new ValidationException("All fields are required");
        }
        if(clientRepository.existsByDni(client.getDni())){
            log.warn("Attempt to register client with existing DNI: {}", client.getDni());
            throw new IllegalArgumentException("Dni is already in use");
        }
        if(clientRepository.existsByEmail(client.getEmail())){
            log.warn("Attempt to register client with existing email: {}", client.getEmail());
            throw new IllegalArgumentException("Email is already in use");
        }

        Client savedClient = clientRepository.save(client);
        log.info("Client registered successfully: {}", savedClient.getId());
        return savedClient;
    }
}
