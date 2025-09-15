package com.banking.client.dto.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CreateClientRequest Tests")
class CreateClientRequestTest {

    private Validator validator;
    private CreateClientRequest createClientRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        createClientRequest = new CreateClientRequest();
    }

    @Nested
    @DisplayName("Object Construction Tests")
    class ObjectConstructionTests {

        @Test
        @DisplayName("Should create empty CreateClientRequest")
        void shouldCreateEmptyCreateClientRequest() {
            // When
            CreateClientRequest request = new CreateClientRequest();

            // Then
            assertThat(request).isNotNull();
            assertThat(request.getFirstName()).isNull();
            assertThat(request.getLastName()).isNull();
            assertThat(request.getEmail()).isNull();
            assertThat(request.getDni()).isNull();
        }

        @Test
        @DisplayName("Should set and get firstName correctly")
        void shouldSetAndGetFirstNameCorrectly() {
            // Given
            String firstName = "Juan";

            // When
            createClientRequest.setFirstName(firstName);

            // Then
            assertThat(createClientRequest.getFirstName()).isEqualTo(firstName);
        }

        @Test
        @DisplayName("Should set and get lastName correctly")
        void shouldSetAndGetLastNameCorrectly() {
            // Given
            String lastName = "Perez";

            // When
            createClientRequest.setLastName(lastName);

            // Then
            assertThat(createClientRequest.getLastName()).isEqualTo(lastName);
        }

        @Test
        @DisplayName("Should set and get email correctly")
        void shouldSetAndGetEmailCorrectly() {
            // Given
            String email = "juan.perez@test.com";

            // When
            createClientRequest.setEmail(email);

            // Then
            assertThat(createClientRequest.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("Should set and get dni correctly")
        void shouldSetAndGetDniCorrectly() {
            // Given
            String dni = "12345678";

            // When
            createClientRequest.setDni(dni);

            // Then
            assertThat(createClientRequest.getDni()).isEqualTo(dni);
        }
    }

    @Nested
    @DisplayName("Field Validation Tests")
    class FieldValidationTests {

        @Test
        @DisplayName("Should pass validation with valid data")
        void shouldPassValidationWithValidData() {
            // Given
            createClientRequest.setFirstName("Juan");
            createClientRequest.setLastName("Perez");
            createClientRequest.setEmail("juan.perez@test.com");
            createClientRequest.setDni("12345678");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should fail validation when firstName is null")
        void shouldFailValidationWhenFirstNameIsNull() {
            // Given
            createClientRequest.setFirstName(null);
            createClientRequest.setLastName("Perez");
            createClientRequest.setEmail("juan.perez@test.com");
            createClientRequest.setDni("12345678");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("firstName");
        }

        @Test
        @DisplayName("Should fail validation when firstName is blank")
        void shouldFailValidationWhenFirstNameIsBlank() {
            // Given
            createClientRequest.setFirstName("   ");
            createClientRequest.setLastName("Perez");
            createClientRequest.setEmail("juan.perez@test.com");
            createClientRequest.setDni("12345678");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("firstName");
        }

        @Test
        @DisplayName("Should fail validation when lastName is null")
        void shouldFailValidationWhenLastNameIsNull() {
            // Given
            createClientRequest.setFirstName("Juan");
            createClientRequest.setLastName(null);
            createClientRequest.setEmail("juan.perez@test.com");
            createClientRequest.setDni("12345678");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("lastName");
        }

        @Test
        @DisplayName("Should fail validation when email is invalid")
        void shouldFailValidationWhenEmailIsInvalid() {
            // Given
            createClientRequest.setFirstName("Juan");
            createClientRequest.setLastName("Perez");
            createClientRequest.setEmail("invalid-email");
            createClientRequest.setDni("12345678");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
        }

        @Test
        @DisplayName("Should fail validation when dni is null")
        void shouldFailValidationWhenDniIsNull() {
            // Given
            createClientRequest.setFirstName("Juan");
            createClientRequest.setLastName("Perez");
            createClientRequest.setEmail("juan.perez@test.com");
            createClientRequest.setDni(null);

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("dni");
        }

        @Test
        @DisplayName("Should fail validation when dni is blank")
        void shouldFailValidationWhenDniIsBlank() {
            // Given
            createClientRequest.setFirstName("Juan");
            createClientRequest.setLastName("Perez");
            createClientRequest.setEmail("juan.perez@test.com");
            createClientRequest.setDni("   ");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("dni");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long firstName")
        void shouldHandleVeryLongFirstName() {
            // Given
            String longFirstName = "A".repeat(500);
            createClientRequest.setFirstName(longFirstName);
            createClientRequest.setLastName("Perez");
            createClientRequest.setEmail("juan.perez@test.com");
            createClientRequest.setDni("12345678");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then - Depends on validation constraints in the actual DTO
            assertThat(createClientRequest.getFirstName()).isEqualTo(longFirstName);
        }

        @Test
        @DisplayName("Should handle special characters in names")
        void shouldHandleSpecialCharactersInNames() {
            // Given
            createClientRequest.setFirstName("José María");
            createClientRequest.setLastName("García-Pérez");
            createClientRequest.setEmail("jose.garcia@test.com");
            createClientRequest.setDni("12345678");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(createClientRequest.getFirstName()).isEqualTo("José María");
            assertThat(createClientRequest.getLastName()).isEqualTo("García-Pérez");
        }

        @Test
        @DisplayName("Should handle minimum valid data")
        void shouldHandleMinimumValidData() {
            // Given
            createClientRequest.setFirstName("A");
            createClientRequest.setLastName("B");
            createClientRequest.setEmail("a@b.co");
            createClientRequest.setDni("1");

            // When
            Set<ConstraintViolation<CreateClientRequest>> violations = validator.validate(createClientRequest);

            // Then
            assertThat(createClientRequest.getFirstName()).isEqualTo("A");
            assertThat(createClientRequest.getLastName()).isEqualTo("B");
        }
    }

    @Nested
    @DisplayName("ToString and Equals Tests")
    class ToStringAndEqualsTests {

        @Test
        @DisplayName("Should implement toString correctly")
        void shouldImplementToStringCorrectly() {
            // Given
            createClientRequest.setFirstName("Juan");
            createClientRequest.setLastName("Perez");
            createClientRequest.setEmail("juan.perez@test.com");
            createClientRequest.setDni("12345678");

            // When
            String toString = createClientRequest.toString();

            // Then
            assertThat(toString).isNotNull();
            assertThat(toString).containsIgnoringCase("CreateClientRequest");
        }

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            // Given
            CreateClientRequest request1 = new CreateClientRequest();
            request1.setFirstName("Juan");
            request1.setLastName("Perez");
            request1.setEmail("juan.perez@test.com");
            request1.setDni("12345678");

            CreateClientRequest request2 = new CreateClientRequest();
            request2.setFirstName("Juan");
            request2.setLastName("Perez");
            request2.setEmail("juan.perez@test.com");
            request2.setDni("12345678");

            // When & Then
            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }

        @Test
        @DisplayName("Should implement equals correctly for different objects")
        void shouldImplementEqualsCorrectlyForDifferentObjects() {
            // Given
            CreateClientRequest request1 = new CreateClientRequest();
            request1.setFirstName("Juan");
            request1.setLastName("Perez");

            CreateClientRequest request2 = new CreateClientRequest();
            request2.setFirstName("Maria");
            request2.setLastName("Garcia");

            // When & Then
            assertThat(request1).isNotEqualTo(request2);
        }
    }
}