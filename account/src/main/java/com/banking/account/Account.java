package com.banking.account;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;

import javax.persistence.*;


@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private double balance = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        this.balance += amount;
    }

}
