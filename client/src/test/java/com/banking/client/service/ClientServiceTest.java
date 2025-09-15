package com.banking.client.service;

import com.banking.client.dto.request.CreateClientRequest;
import com.banking.client.model.Client;
import com.banking.client.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientService Tests")
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountServiceClient accountServiceClient;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;
    private CreateClientRequest createClientRequest;

    @BeforeEach
    void setUp() {
        testClient = Client.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Perez")
                .email("juan.perez@test.com")
                .dni("12345678")
                .build();

        createClientRequest = new CreateClientRequest();
        createClientRequest.setFirstName("Juan");
        createClientRequest.setLastName("Perez");
        createClientRequest.setEmail("juan.perez@test.com");
        createClientRequest.setDni("12345678");
    }

    @Nested
    @DisplayName("Register Client Tests")
    class RegisterClientTests {

        @Test
        @DisplayName("Should register client successfully")
        void shouldRegisterClientSuccessfully() {
            // Given
            when(clientRepository.existsByDni(anyString())).thenReturn(false);
            when(clientRepository.existsByEmail(anyString())).thenReturn(false);
            when(clientRepository.save(any(Client.class))).thenReturn(testClient);

            // When
            Client result = clientService.register(createClientRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("Juan");
            assertThat(result.getLastName()).isEqualTo("Perez");
            assertThat(result.getEmail()).isEqualTo("juan.perez@test.com");
            assertThat(result.getDni()).isEqualTo("12345678");

            verify(clientRepository).existsByDni("12345678");
            verify(clientRepository).existsByEmail("juan.perez@test.com");
            verify(clientRepository).save(any(Client.class));
        }

        @Test
        @DisplayName("Should throw ValidationException when firstName is blank")
        void shouldThrowValidationExceptionWhenFirstNameIsBlank() {
            // Given
            createClientRequest.setFirstName("");

            // When & Then
            assertThatThrownBy(() -> clientService.register(createClientRequest))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("All fields are required");

            verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Should throw ValidationException when lastName is null")
        void shouldThrowValidationExceptionWhenLastNameIsNull() {
            // Given
            createClientRequest.setLastName(null);

            // When & Then
            assertThatThrownBy(() -> clientService.register(createClientRequest))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("All fields are required");

            verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when DNI already exists")
        void shouldThrowIllegalArgumentExceptionWhenDniAlreadyExists() {
            // Given
            when(clientRepository.existsByDni(anyString())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> clientService.register(createClientRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Dni is already in use");

            verify(clientRepository).existsByDni("12345678");
            verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when email already exists")
        void shouldThrowIllegalArgumentExceptionWhenEmailAlreadyExists() {
            // Given
            when(clientRepository.existsByDni(anyString())).thenReturn(false);
            when(clientRepository.existsByEmail(anyString())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> clientService.register(createClientRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Email is already in use");

            verify(clientRepository).existsByDni("12345678");
            verify(clientRepository).existsByEmail("juan.perez@test.com");
            verify(clientRepository, never()).save(any(Client.class));
        }
    }

    @Nested
    @DisplayName("Get Client Tests")
    class GetClientTests {

        @Test
        @DisplayName("Should get client by ID successfully")
        void shouldGetClientByIdSuccessfully() {
            // Given
            when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

            // When
            Client result = clientService.get(1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("Juan");
            assertThat(result.getLastName()).isEqualTo("Perez");

            verify(clientRepository).findById(1L);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when client not found")
        void shouldThrowIllegalArgumentExceptionWhenClientNotFound() {
            // Given
            when(clientRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> clientService.get(999L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Client not found");

            verify(clientRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("List Clients Tests")
    class ListClientsTests {

        @Test
        @DisplayName("Should list all clients successfully")
        void shouldListAllClientsSuccessfully() {
            // Given
            Client client2 = Client.builder()
                    .id(2L)
                    .firstName("Maria")
                    .lastName("Garcia")
                    .email("maria.garcia@test.com")
                    .dni("87654321")
                    .build();

            List<Client> clients = Arrays.asList(testClient, client2);
            when(clientRepository.findAll()).thenReturn(clients);

            // When
            List<Client> result = clientService.list();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(Client::getFirstName)
                    .containsExactly("Juan", "Maria");

            verify(clientRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no clients exist")
        void shouldReturnEmptyListWhenNoClientsExist() {
            // Given
            when(clientRepository.findAll()).thenReturn(Arrays.asList());

            // When
            List<Client> result = clientService.list();

            // Then
            assertThat(result).isEmpty();
            verify(clientRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Update Client Tests")
    class UpdateClientTests {

        @Test
        @DisplayName("Should update client successfully")
        void shouldUpdateClientSuccessfully() {
            // Given
            when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
            when(clientRepository.existsByEmail("new.email@test.com")).thenReturn(false);
            when(clientRepository.save(any(Client.class))).thenReturn(testClient);

            // When
            Client result = clientService.updateClient(1L, "Carlos", "Rodriguez", "new.email@test.com");

            // Then
            assertThat(result).isNotNull();
            verify(clientRepository).findById(1L);
            verify(clientRepository).existsByEmail("new.email@test.com");
            verify(clientRepository).save(testClient);
        }

        @Test
        @DisplayName("Should update only non-null fields")
        void shouldUpdateOnlyNonNullFields() {
            // Given
            when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
            when(clientRepository.save(any(Client.class))).thenReturn(testClient);

            // When
            Client result = clientService.updateClient(1L, "Carlos", null, null);

            // Then
            assertThat(result).isNotNull();
            verify(clientRepository).findById(1L);
            verify(clientRepository, never()).existsByEmail(anyString());
            verify(clientRepository).save(testClient);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when email already exists")
        void shouldThrowIllegalArgumentExceptionWhenEmailAlreadyExists() {
            // Given
            when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
            when(clientRepository.existsByEmail("existing@test.com")).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> clientService.updateClient(1L, null, null, "existing@test.com"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Email is already in use");

            verify(clientRepository).findById(1L);
            verify(clientRepository).existsByEmail("existing@test.com");
            verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when client not found")
        void shouldThrowIllegalArgumentExceptionWhenClientNotFoundForUpdate() {
            // Given
            when(clientRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> clientService.updateClient(999L, "Carlos", null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Client with ID 999 not found");

            verify(clientRepository).findById(999L);
            verify(clientRepository, never()).save(any(Client.class));
        }
    }

    @Nested
    @DisplayName("Delete Client Tests")
    class DeleteClientTests {

        @Test
        @DisplayName("Should delete client successfully")
        void shouldDeleteClientSuccessfully() {
            // Given
            when(clientRepository.existsById(1L)).thenReturn(true);
            when(accountServiceClient.hasAccounts(1L)).thenReturn(false);

            // When
            clientService.deleteClient(1L);

            // Then
            verify(clientRepository).existsById(1L);
            verify(accountServiceClient).hasAccounts(1L);
            verify(clientRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when client not found")
        void shouldThrowIllegalArgumentExceptionWhenClientNotFoundForDelete() {
            // Given
            when(clientRepository.existsById(999L)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> clientService.deleteClient(999L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Client not found");

            verify(clientRepository).existsById(999L);
            verify(accountServiceClient, never()).hasAccounts(anyLong());
            verify(clientRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw ValidationException when client has active accounts")
        void shouldThrowValidationExceptionWhenClientHasActiveAccounts() {
            // Given
            when(clientRepository.existsById(1L)).thenReturn(true);
            when(accountServiceClient.hasAccounts(1L)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> clientService.deleteClient(1L))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Cannot delete client with active accounts");

            verify(clientRepository).existsById(1L);
            verify(accountServiceClient).hasAccounts(1L);
            verify(clientRepository, never()).deleteById(anyLong());
        }
    }
}