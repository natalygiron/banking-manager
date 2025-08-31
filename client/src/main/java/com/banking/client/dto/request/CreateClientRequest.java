package com.banking.client.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "DNI is required")
    private String dni;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
}
