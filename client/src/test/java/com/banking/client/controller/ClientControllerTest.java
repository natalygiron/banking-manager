package com.banking.client.controller;

import com.banking.client.dto.request.CreateClientRequest;
import com.banking.client.dto.request.UpdateClientRequest;
import com.banking.client.model.Client;
import com.banking.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@DisplayName("ClientController Tests")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Client testClient;
    private CreateClientRequest createClientRequest;
    private UpdateClientRequest updateClientRequest;

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

        updateClientRequest = new UpdateClientRequest();
        updateClientRequest.setFirstName("Carlos");
        updateClientRequest.setLastName("Rodriguez");
        updateClientRequest.setEmail("carlos.rodriguez@test.com");
    }

    @Nested
    @DisplayName("Create Client Tests")
    class CreateClientTests {

        @Test
        @DisplayName("Should create client successfully")
        void shouldCreateClientSuccessfully() throws Exception {
            // Given
            when(clientService.register(any(CreateClientRequest.class))).thenReturn(testClient);

            // When & Then
            mockMvc.perform(post("/api/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createClientRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.firstName").value("Juan"))
                    .andExpect(jsonPath("$.lastName").value("Perez"))
                    .andExpect(jsonPath("$.email").value("juan.perez@test.com"))
                    .andExpect(jsonPath("$.dni").value("12345678"));

            verify(clientService).register(any(CreateClientRequest.class));
        }

        @Test
        @DisplayName("Should return 400 when validation fails")
        void shouldReturn400WhenValidationFails() throws Exception {
            // Given
            when(clientService.register(any(CreateClientRequest.class)))
                    .thenThrow(new ValidationException("All fields are required"));

            // When & Then
            mockMvc.perform(post("/api/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createClientRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("All fields are required"));

            verify(clientService).register(any(CreateClientRequest.class));
        }

        @Test
        @DisplayName("Should return 400 when DNI already exists")
        void shouldReturn400WhenDniAlreadyExists() throws Exception {
            // Given
            when(clientService.register(any(CreateClientRequest.class)))
                    .thenThrow(new IllegalArgumentException("Dni is already in use"));

            // When & Then
            mockMvc.perform(post("/api/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createClientRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Dni is already in use"));

            verify(clientService).register(any(CreateClientRequest.class));
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            // Given
            String invalidJson = "{\"firstName\": \"\", \"lastName\": null}";

            // When & Then
            mockMvc.perform(post("/api/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Get Client Tests")
    class GetClientTests {

        @Test
        @DisplayName("Should get client by ID successfully")
        void shouldGetClientByIdSuccessfully() throws Exception {
            // Given
            when(clientService.get(1L)).thenReturn(testClient);

            // When & Then
            mockMvc.perform(get("/api/clients/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.firstName").value("Juan"))
                    .andExpect(jsonPath("$.lastName").value("Perez"))
                    .andExpect(jsonPath("$.email").value("juan.perez@test.com"))
                    .andExpect(jsonPath("$.dni").value("12345678"));

            verify(clientService).get(1L);
        }

        @Test
        @DisplayName("Should return 404 when client not found")
        void shouldReturn404WhenClientNotFound() throws Exception {
            // Given
            when(clientService.get(999L))
                    .thenThrow(new IllegalArgumentException("Client not found"));

            // When & Then
            mockMvc.perform(get("/api/clients/{id}", 999L))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Client not found"));

            verify(clientService).get(999L);
        }
    }

    @Nested
    @DisplayName("List Clients Tests")
    class ListClientsTests {

        @Test
        @DisplayName("Should list all clients successfully")
        void shouldListAllClientsSuccessfully() throws Exception {
            // Given
            Client client2 = Client.builder()
                    .id(2L)
                    .firstName("Maria")
                    .lastName("Garcia")
                    .email("maria.garcia@test.com")
                    .dni("87654321")
                    .build();

            List<Client> clients = Arrays.asList(testClient, client2);
            when(clientService.list()).thenReturn(clients);

            // When & Then
            mockMvc.perform(get("/api/clients"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].firstName").value("Juan"))
                    .andExpect(jsonPath("$[1].id").value(2L))
                    .andExpect(jsonPath("$[1].firstName").value("Maria"));

            verify(clientService).list();
        }

        @Test
        @DisplayName("Should return empty list when no clients exist")
        void shouldReturnEmptyListWhenNoClientsExist() throws Exception {
            // Given
            when(clientService.list()).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/clients"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(clientService).list();
        }
    }

    @Nested
    @DisplayName("Update Client Tests")
    class UpdateClientTests {

        @Test
        @DisplayName("Should update client successfully with PUT")
        void shouldUpdateClientSuccessfullyWithPut() throws Exception {
            // Given
            Client updatedClient = Client.builder()
                    .id(1L)
                    .firstName("Carlos")
                    .lastName("Rodriguez")
                    .email("carlos.rodriguez@test.com")
                    .dni("12345678")
                    .build();

            when(clientService.updateClient(eq(1L), anyString(), anyString(), anyString()))
                    .thenReturn(updatedClient);

            // When & Then
            mockMvc.perform(put("/api/clients/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateClientRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.firstName").value("Carlos"))
                    .andExpect(jsonPath("$.lastName").value("Rodriguez"))
                    .andExpect(jsonPath("$.email").value("carlos.rodriguez@test.com"));

            verify(clientService).updateClient(1L, "Carlos", "Rodriguez", "carlos.rodriguez@test.com");
        }

        @Test
        @DisplayName("Should update client successfully with PATCH")
        void shouldUpdateClientSuccessfullyWithPatch() throws Exception {
            // Given
            updateClientRequest.setLastName(null); // Partial update
            updateClientRequest.setEmail(null);

            when(clientService.updateClient(eq(1L), eq("Carlos"), isNull(), isNull()))
                    .thenReturn(testClient);

            // When & Then
            mockMvc.perform(patch("/api/clients/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateClientRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.firstName").value("Juan")); // Original values

            verify(clientService).updateClient(1L, "Carlos", null, null);
        }

        @Test
        @DisplayName("Should return 404 when client not found for update")
        void shouldReturn404WhenClientNotFoundForUpdate() throws Exception {
            // Given
            when(clientService.updateClient(eq(999L), anyString(), anyString(), anyString()))
                    .thenThrow(new IllegalArgumentException("Client with ID 999 not found"));

            // When & Then
            mockMvc.perform(put("/api/clients/{id}", 999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateClientRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Client with ID 999 not found"));

            verify(clientService).updateClient(999L, "Carlos", "Rodriguez", "carlos.rodriguez@test.com");
        }

        @Test
        @DisplayName("Should return 400 when email already exists for update")
        void shouldReturn400WhenEmailAlreadyExistsForUpdate() throws Exception {
            // Given
            when(clientService.updateClient(eq(1L), anyString(), anyString(), anyString()))
                    .thenThrow(new IllegalArgumentException("Email is already in use"));

            // When & Then
            mockMvc.perform(put("/api/clients/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateClientRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Email is already in use"));

            verify(clientService).updateClient(1L, "Carlos", "Rodriguez", "carlos.rodriguez@test.com");
        }
    }

    @Nested
    @DisplayName("Delete Client Tests")
    class DeleteClientTests {

        @Test
        @DisplayName("Should delete client successfully")
        void shouldDeleteClientSuccessfully() throws Exception {
            // Given
            doNothing().when(clientService).deleteClient(1L);

            // When & Then
            mockMvc.perform(delete("/api/clients/{id}", 1L))
                    .andExpect(status().isNoContent());

            verify(clientService).deleteClient(1L);
        }

        @Test
        @DisplayName("Should return 404 when client not found for delete")
        void shouldReturn404WhenClientNotFoundForDelete() throws Exception {
            // Given
            doThrow(new IllegalArgumentException("Client not found"))
                    .when(clientService).deleteClient(999L);

            // When & Then
            mockMvc.perform(delete("/api/clients/{id}", 999L))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Client not found"));

            verify(clientService).deleteClient(999L);
        }

        @Test
        @DisplayName("Should return 400 when client has active accounts")
        void shouldReturn400WhenClientHasActiveAccounts() throws Exception {
            // Given
            doThrow(new ValidationException("Cannot delete client with active accounts"))
                    .when(clientService).deleteClient(1L);

            // When & Then
            mockMvc.perform(delete("/api/clients/{id}", 1L))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Cannot delete client with active accounts"));

            verify(clientService).deleteClient(1L);
        }
    }
}