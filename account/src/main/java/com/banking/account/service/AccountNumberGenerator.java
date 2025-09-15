package com.banking.account.service;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;
import com.banking.account.repository.AccountRepository;
import lombok.AllArgsConstructor;

// Generador de número de cuenta
@Component
@AllArgsConstructor
public class AccountNumberGenerator {
    private final SecureRandom secureRandom = new SecureRandom();
    private final AccountRepository accountRepository;

    public String generate() {
        String acc;
        do {
            acc = "ACC-" + tenDigits();
        } while (accountRepository.existsByAccountNumber(acc));
        return acc;
    }

    private String tenDigits() {
        long n = Math.abs(secureRandom.nextLong()) % 1_000_000_0000L;
        return String.format("%010d", n);
    }
}
