package com.banking.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
}