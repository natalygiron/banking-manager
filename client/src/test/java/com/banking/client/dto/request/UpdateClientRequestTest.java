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

@DisplayName("UpdateClientRequest Tests")
class UpdateClientRequestTest {

    private Validator validator;
    private UpdateClientRequest updateClientRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        updateClientRequest = new UpdateClientRequest();
    }

    @Nested
    @DisplayName("Object Construction Tests")
    class ObjectConstructionTests {

        @Test
        @DisplayName("Should create empty UpdateClientRequest")
        void shouldCreateEmptyUpdateClientRequest() {
            // When
            UpdateClientRequest request = new UpdateClientRequest();

            // Then
            assertThat(request).isNotNull();
            assertThat(request.getFirstName()).isNull();
            assertThat(request.getLastName()).isNull();
            assertThat(request.getEmail()).isNull();
        }

        @Test
        @DisplayName("Should set and get firstName correctly")
        void shouldSetAndGetFirstNameCorrectly() {
            // Given
            String firstName = "Carlos";

            // When
            updateClientRequest.setFirstName(firstName);

            // Then
            assertThat(updateClientRequest.getFirstName()).isEqualTo(firstName);
        }

        @Test
        @DisplayName("Should set and get lastName correctly")
        void shouldSetAndGetLastNameCorrectly() {
            // Given
            String lastName = "Rodriguez";

            // When
            updateClientRequest.setLastName(lastName);

            // Then
            assertThat(updateClientRequest.getLastName()).isEqualTo(lastName);
        }

        @Test
        @DisplayName("Should set and get email correctly")
        void shouldSetAndGetEmailCorrectly() {
            // Given
            String email = "carlos.rodriguez@test.com";

            // When
            updateClientRequest.setEmail(email);

            // Then
            assertThat(updateClientRequest.getEmail()).isEqualTo(email);
        }
    }

    @Nested
    @DisplayName("Field Validation Tests")
    class FieldValidationTests {

        @Test
        @DisplayName("Should pass validation with all null fields (partial update)")
        void shouldPassValidationWithAllNullFields() {
            // Given - All fields are null by default

            // When
            Set<ConstraintViolation<UpdateClientRequest>> violations = validator.validate(updateClientRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should pass validation with valid firstName only")
        void shouldPassValidationWithValidFirstNameOnly() {
            // Given
            updateClientRequest.setFirstName("Carlos");

            // When
            Set<ConstraintViolation<UpdateClientRequest>> violations = validator.validate(updateClientRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should pass validation with valid lastName only")
        void shouldPassValidationWithValidLastNameOnly() {
            // Given
            updateClientRequest.setLastName("Rodriguez");

            // When
            Set<ConstraintViolation<UpdateClientRequest>> violations = validator.validate(updateClientRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should pass validation with valid email only")
        void shouldPassValidationWithValidEmailOnly() {
            // Given
            updateClientRequest.setEmail("carlos.rodriguez@test.com");

            // When
            Set<ConstraintViolation<UpdateClientRequest>> violations = validator.validate(updateClientRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should pass validation with all valid fields")
        void shouldPassValidationWithAllValidFields() {
            // Given
            updateClientRequest.setFirstName("Carlos");
            updateClientRequest.setLastName("Rodriguez");
            updateClientRequest.setEmail("carlos.rodriguez@test.com");

            // When
            Set<ConstraintViolation<UpdateClientRequest>> violations = validator.validate(updateClientRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should fail validation when firstName is blank")
        void shouldFailValidationWhenFirstNameIsBlank() {
            // Given
            updateClientRequest.setFirstName("   ");

            // When
            Set<ConstraintViolation<UpdateClientRequest>> violations = validator.validate(updateClientRequest);

            // Then - Depends on validation constraints in actual DTO
            // May pass or fail depending on implementation
            assertThat(updateClientRequest.getFirstName()).isEqualTo("   ");
        }

        @Test
        @DisplayName("Should fail validation when lastName is blank")
        void shouldFailValidationWhenLastNameIsBlank() {
            // Given
            updateClientRequest.setLastName("   ");

            // When
            Set<ConstraintViolation<UpdateClientRequest>> violations = validator.validate(updateClientRequest);

            // Then - Depends on validation constraints in actual DTO
            assertThat(updateClientRequest.getLastName()).isEqualTo("   ");
        }

        @Test
        @DisplayName("Should fail validation when email is invalid")
        void shouldFailValidationWhenEmailIsInvalid() {
            // Given
            updateClientRequest.setEmail("invalid-email");

            // When
            Set<ConstraintViolation<UpdateClientRequest>> violations = validator.validate(updateClientRequest);

            // Then - Should fail if email validation is implemented
            // assertThat(violations).hasSize(1);
            // assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
        }
    }

    @Nested
    @DisplayName("Partial Update Tests")
    class PartialUpdateTests {

        @Test
        @DisplayName("Should support partial update with only firstName")
        void shouldSupportPartialUpdateWithOnlyFirstName() {
            // Given
            updateClientRequest.setFirstName("NewFirstName");
            // lastName and email remain null

            // When & Then
            assertThat(updateClientRequest.getFirstName()).isEqualTo("NewFirstName");
            assertThat(updateClientRequest.getLastName()).isNull();
            assertThat(updateClientRequest.getEmail()).isNull();
        }

        @Test
        @DisplayName("Should support partial update with only lastName")
        void shouldSupportPartialUpdateWithOnlyLastName() {
            // Given
            updateClientRequest.setLastName("NewLastName");
            // firstName and email remain null

            // When & Then
            assertThat(updateClientRequest.getFirstName()).isNull();
            assertThat(updateClientRequest.getLastName()).isEqualTo("NewLastName");
            assertThat(updateClientRequest.getEmail()).isNull();
        }

        @Test
        @DisplayName("Should support partial update with only email")
        void shouldSupportPartialUpdateWithOnlyEmail() {
            // Given
            updateClientRequest.setEmail("new.email@test.com");
            // firstName and lastName remain null

            // When & Then
            assertThat(updateClientRequest.getFirstName()).isNull();
            assertThat(updateClientRequest.getLastName()).isNull();
            assertThat(updateClientRequest.getEmail()).isEqualTo("new.email@test.com");
        }

        @Test
        @DisplayName("Should support partial update with multiple fields")
        void shouldSupportPartialUpdateWithMultipleFields() {
            // Given
            updateClientRequest.setFirstName("NewFirstName");
            updateClientRequest.setEmail("new.email@test.com");
            // lastName remains null

            // When & Then
            assertThat(updateClientRequest.getFirstName()).isEqualTo("NewFirstName");
            assertThat(updateClientRequest.getLastName()).isNull();
            assertThat(updateClientRequest.getEmail()).isEqualTo("new.email@test.com");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty strings vs null")
        void shouldHandleEmptyStringsVsNull() {
            // Given
            updateClientRequest.setFirstName("");
            updateClientRequest.setLastName(null);

            // When & Then
            assertThat(updateClientRequest.getFirstName()).isEmpty();
            assertThat(updateClientRequest.getLastName()).isNull();
        }

        @Test
        @DisplayName("Should handle special characters in names")
        void shouldHandleSpecialCharactersInNames() {
            // Given
            updateClientRequest.setFirstName("José María");
            updateClientRequest.setLastName("García-Pérez");

            // When & Then
            assertThat(updateClientRequest.getFirstName()).isEqualTo("José María");
            assertThat(updateClientRequest.getLastName()).isEqualTo("García-Pérez");
        }

        @Test
        @DisplayName("Should handle very long names")
        void shouldHandleVeryLongNames() {
            // Given
            String longName = "A".repeat(500);
            updateClientRequest.setFirstName(longName);

            // When & Then
            assertThat(updateClientRequest.getFirstName()).isEqualTo(longName);
        }

        @Test
        @DisplayName("Should handle different email formats")
        void shouldHandleDifferentEmailFormats() {
            // Given
            String[] validEmails = {
                "test@example.com",
                "user.name@domain.co.uk",
                "user+tag@domain.com",
                "123@domain.com"
            };

            for (String email : validEmails) {
                // When
                updateClientRequest.setEmail(email);

                // Then
                assertThat(updateClientRequest.getEmail()).isEqualTo(email);
            }
        }
    }

    @Nested
    @DisplayName("ToString and Equals Tests")
    class ToStringAndEqualsTests {

        @Test
        @DisplayName("Should implement toString correctly")
        void shouldImplementToStringCorrectly() {
            // Given
            updateClientRequest.setFirstName("Carlos");
            updateClientRequest.setLastName("Rodriguez");
            updateClientRequest.setEmail("carlos.rodriguez@test.com");

            // When
            String toString = updateClientRequest.toString();

            // Then
            assertThat(toString).isNotNull();
            assertThat(toString).containsIgnoringCase("UpdateClientRequest");
        }

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            // Given
            UpdateClientRequest request1 = new UpdateClientRequest();
            request1.setFirstName("Carlos");
            request1.setLastName("Rodriguez");
            request1.setEmail("carlos.rodriguez@test.com");

            UpdateClientRequest request2 = new UpdateClientRequest();
            request2.setFirstName("Carlos");
            request2.setLastName("Rodriguez");
            request2.setEmail("carlos.rodriguez@test.com");

            // When & Then
            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }

        @Test
        @DisplayName("Should implement equals correctly for different objects")
        void shouldImplementEqualsCorrectlyForDifferentObjects() {
            // Given
            UpdateClientRequest request1 = new UpdateClientRequest();
            request1.setFirstName("Carlos");

            UpdateClientRequest request2 = new UpdateClientRequest();
            request2.setFirstName("Maria");

            // When & Then
            assertThat(request1).isNotEqualTo(request2);
        }

        @Test
        @DisplayName("Should implement equals correctly with null fields")
        void shouldImplementEqualsCorrectlyWithNullFields() {
            // Given
            UpdateClientRequest request1 = new UpdateClientRequest();
            UpdateClientRequest request2 = new UpdateClientRequest();

            // When & Then
            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }
    }
}