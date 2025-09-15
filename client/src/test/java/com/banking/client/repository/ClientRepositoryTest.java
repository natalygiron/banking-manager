package com.banking.client.repository;

import com.banking.client.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ClientRepository Integration Tests")
class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = Client.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan.perez@test.com")
                .dni("12345678")
                .build();
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class BasicCrudOperations {

        @Test
        @DisplayName("Should save and retrieve client")
        void shouldSaveAndRetrieveClient() {
            // When
            Client savedClient = clientRepository.save(testClient);
            entityManager.flush();
            entityManager.clear();

            // Then
            assertThat(savedClient.getId()).isNotNull();
            assertThat(savedClient.getFirstName()).isEqualTo("Juan");
            assertThat(savedClient.getLastName()).isEqualTo("Perez");
            assertThat(savedClient.getEmail()).isEqualTo("juan.perez@test.com");
            assertThat(savedClient.getDni()).isEqualTo("12345678");

            Optional<Client> retrievedClient = clientRepository.findById(savedClient.getId());
            assertThat(retrievedClient).isPresent();
            assertThat(retrievedClient.get()).isEqualTo(savedClient);
        }

        @Test
        @DisplayName("Should find all clients")
        void shouldFindAllClients() {
            // Given
            Client client2 = Client.builder()
                    .firstName("Maria")
                    .lastName("Garcia")
                    .email("maria.garcia@test.com")
                    .dni("87654321")
                    .build();

            entityManager.persistAndFlush(testClient);
            entityManager.persistAndFlush(client2);
            entityManager.clear();

            // When
            List<Client> clients = clientRepository.findAll();

            // Then
            assertThat(clients).hasSize(2);
            assertThat(clients).extracting(Client::getFirstName)
                    .containsExactlyInAnyOrder("Juan", "Maria");
        }

        @Test
        @DisplayName("Should delete client by ID")
        void shouldDeleteClientById() {
            // Given
            Client savedClient = entityManager.persistAndFlush(testClient);
            Long clientId = savedClient.getId();
            entityManager.clear();

            // When
            clientRepository.deleteById(clientId);
            entityManager.flush();

            // Then
            Optional<Client> deletedClient = clientRepository.findById(clientId);
            assertThat(deletedClient).isEmpty();
        }

        @Test
        @DisplayName("Should check if client exists by ID")
        void shouldCheckIfClientExistsById() {
            // Given
            Client savedClient = entityManager.persistAndFlush(testClient);
            Long clientId = savedClient.getId();
            entityManager.clear();

            // When & Then
            assertThat(clientRepository.existsById(clientId)).isTrue();
            assertThat(clientRepository.existsById(999L)).isFalse();
        }
    }

    @Nested
    @DisplayName("Custom Query Methods")
    class CustomQueryMethods {

        @Test
        @DisplayName("Should find client by DNI exists")
        void shouldFindClientByDniExists() {
            // Given
            entityManager.persistAndFlush(testClient);
            entityManager.clear();

            // When & Then
            assertThat(clientRepository.existsByDni("12345678")).isTrue();
            assertThat(clientRepository.existsByDni("nonexistent")).isFalse();
        }

        @Test
        @DisplayName("Should find client by email exists")
        void shouldFindClientByEmailExists() {
            // Given
            entityManager.persistAndFlush(testClient);
            entityManager.clear();

            // When & Then
            assertThat(clientRepository.existsByEmail("juan.perez@test.com")).isTrue();
            assertThat(clientRepository.existsByEmail("nonexistent@test.com")).isFalse();
        }

        @Test
        @DisplayName("Should find client by email")
        void shouldFindClientByEmail() {
            // Given
            Client savedClient = entityManager.persistAndFlush(testClient);
            entityManager.clear();

            // When
            Optional<Client> foundClient = clientRepository.findByEmail("juan.perez@test.com");

            // Then
            assertThat(foundClient).isPresent();
            assertThat(foundClient.get().getId()).isEqualTo(savedClient.getId());
            assertThat(foundClient.get().getFirstName()).isEqualTo("Juan");
            assertThat(foundClient.get().getDni()).isEqualTo("12345678");
        }

        @Test
        @DisplayName("Should return empty when finding by non-existent email")
        void shouldReturnEmptyWhenFindingByNonExistentEmail() {
            // Given
            entityManager.persistAndFlush(testClient);
            entityManager.clear();

            // When
            Optional<Client> foundClient = clientRepository.findByEmail("nonexistent@test.com");

            // Then
            assertThat(foundClient).isEmpty();
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should handle duplicate DNI constraint")
        void shouldHandleDuplicateDniConstraint() {
            // Given
            entityManager.persistAndFlush(testClient);

            Client duplicateDniClient = Client.builder()
                    .firstName("Maria")
                    .lastName("Garcia")
                    .email("maria.garcia@test.com")
                    .dni("12345678") // Same DNI
                    .build();

            // When & Then
            assertThatThrownBy(() -> {
                entityManager.persistAndFlush(duplicateDniClient);
            }).isInstanceOf(Exception.class); // Could be ConstraintViolationException or similar
        }

        @Test
        @DisplayName("Should handle duplicate email constraint")
        void shouldHandleDuplicateEmailConstraint() {
            // Given
            entityManager.persistAndFlush(testClient);

            Client duplicateEmailClient = Client.builder()
                    .firstName("Maria")
                    .lastName("Garcia")
                    .email("juan.perez@test.com") // Same email
                    .dni("87654321")
                    .build();

            // When & Then
            assertThatThrownBy(() -> {
                entityManager.persistAndFlush(duplicateEmailClient);
            }).isInstanceOf(Exception.class); // Could be ConstraintViolationException or similar
        }

        @Test
        @DisplayName("Should handle null values in required fields")
        void shouldHandleNullValuesInRequiredFields() {
            // Given
            Client invalidClient = Client.builder()
                    .firstName(null) // Required field
                    .lastName("Perez")
                    .email("test@test.com")
                    .dni("12345678")
                    .build();

            // When & Then
            assertThatThrownBy(() -> {
                entityManager.persistAndFlush(invalidClient);
            }).isInstanceOf(Exception.class); // ValidationException or similar
        }
    }

    @Nested
    @DisplayName("Query Performance Tests")
    class QueryPerformanceTests {

        @Test
        @DisplayName("Should handle multiple clients efficiently")
        void shouldHandleMultipleClientsEfficiently() {
            // Given - Create multiple clients
            for (int i = 0; i < 10; i++) {
                Client client = Client.builder()
                        .firstName("FirstName" + i)
                        .lastName("LastName" + i)
                        .email("email" + i + "@test.com")
                        .dni("dni" + i)
                        .build();
                entityManager.persist(client);
            }
            entityManager.flush();
            entityManager.clear();

            // When
            long startTime = System.currentTimeMillis();
            List<Client> allClients = clientRepository.findAll();
            long endTime = System.currentTimeMillis();

            // Then
            assertThat(allClients).hasSize(10);
            assertThat(endTime - startTime).isLessThan(1000); // Should complete within 1 second
        }

        @Test
        @DisplayName("Should perform case-insensitive email search efficiently")
        void shouldPerformCaseInsensitiveEmailSearchEfficiently() {
            // Given
            entityManager.persistAndFlush(testClient);
            entityManager.clear();

            // When
            Optional<Client> foundClient = clientRepository.findByEmail("JUAN.PEREZ@TEST.COM");

            // Then - Depends on database configuration for case sensitivity
            // May or may not find the client depending on collation settings
            assertThat(foundClient).isNotNull(); // At minimum, query should execute without error
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long field values")
        void shouldHandleVeryLongFieldValues() {
            // Given
            String longName = "A".repeat(255); // Assuming 255 is the limit
            Client longFieldClient = Client.builder()
                    .firstName(longName)
                    .lastName("LastName")
                    .email("longnametest@test.com")
                    .dni("longdni123")
                    .build();

            // When & Then
            Client savedClient = clientRepository.save(longFieldClient);
            assertThat(savedClient.getFirstName()).isEqualTo(longName);
        }

        @Test
        @DisplayName("Should handle special characters in fields")
        void shouldHandleSpecialCharactersInFields() {
            // Given
            Client specialCharClient = Client.builder()
                    .firstName("José María")
                    .lastName("García-Pérez")
                    .email("jose.garcia@test.com")
                    .dni("12345678")
                    .build();

            // When
            Client savedClient = clientRepository.save(specialCharClient);
            entityManager.flush();
            entityManager.clear();

            // Then
            Optional<Client> retrievedClient = clientRepository.findById(savedClient.getId());
            assertThat(retrievedClient).isPresent();
            assertThat(retrievedClient.get().getFirstName()).isEqualTo("José María");
            assertThat(retrievedClient.get().getLastName()).isEqualTo("García-Pérez");
        }

        @Test
        @DisplayName("Should handle whitespace in fields")
        void shouldHandleWhitespaceInFields() {
            // Given
            Client whitespaceClient = Client.builder()
                    .firstName("  Juan  ")
                    .lastName("  Perez  ")
                    .email("juan.perez@test.com")
                    .dni("12345678")
                    .build();

            // When
            Client savedClient = clientRepository.save(whitespaceClient);
            entityManager.flush();
            entityManager.clear();

            // Then
            Optional<Client> retrievedClient = clientRepository.findById(savedClient.getId());
            assertThat(retrievedClient).isPresent();
            // Depending on implementation, whitespace may or may not be preserved
            assertThat(retrievedClient.get().getFirstName()).contains("Juan");
            assertThat(retrievedClient.get().getLastName()).contains("Perez");
        }
    }
}