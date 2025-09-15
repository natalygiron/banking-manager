package com.banking.client.dto.request;

import lombok.*;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchClientRequest {
    private String firstName;
    private String lastName;
    @Email(message = "Email must be valid")
    private String email;
}