package com.banking.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchClientRequest {

    private String firstName;
    private String lastName;
    private String dni;
    @Email(message = "Email must be valid")
    private String email;
}
