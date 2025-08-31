package com.banking.client.dto.response;

import com.banking.client.Client;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;

    public static CreateClientResponse from(Client client) {
        return new CreateClientResponse(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getDni(),
                client.getEmail()
        );
    }
}