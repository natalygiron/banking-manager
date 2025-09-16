package com.banking.client.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(
        name = "clients",
        indexes = {
                @Index( name = "ux_clients_email",
                        columnList = "email",
                        unique = true),
                @Index( name = "ux_clients_dni",
                        columnList = "dni",
                        unique = true)
        }
)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String dni;
}
