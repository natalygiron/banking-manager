package com.banking.account;

import com.banking.account.dto.request.CreateAccountRequest;
import com.banking.account.dto.response.ClientResponse;
import com.banking.account.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.transaction.annotation.Transactional;
import javax.validation.ValidationException;
import java.security.SecureRandom;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        validateRequest(request);

        if (!clientExists(request.getClientId())) {
            throw new IllegalArgumentException("Client with ID " + request.getClientId() + " does not exist");
        }

        String accountNumber = generateUniqueAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .type(request.getType())
                .balance(0.0)
                .clientId(request.getClientId())
                .build();

        account.deposit(request.getInitialBalance());

        Account saved = accountRepository.save(account);
        log.info("Account created: {}", saved.getAccountNumber());
        return saved;
    }

    @Transactional(readOnly = true)
    public Account get(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Transactional
    public Account deposit(Long accountId, double amount) {
        validateAmount(amount);

        Account account = get(accountId);
        account.deposit(amount);

        Account updated = accountRepository.save(account);
        log.info("Deposit of {} made to account {}", amount, updated.getAccountNumber());
        return updated;
    }

    @Transactional
    public Account withdraw(Long accountId, double amount) {
        validateAmount(amount);

        Account account = get(accountId);
        account.withdraw(amount);

        Account updated = accountRepository.save(account);
        log.info("Withdrawal of {} made from account {}", amount, updated.getAccountNumber());
        return updated;
    }

    @Transactional(readOnly = true)
    public List<Account> listAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Account> listByClient(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new ValidationException("Amount must be greater than 0");
        }
    }

    private void validateRequest(CreateAccountRequest request) {
        if (request.getType() == null) {
            throw new ValidationException("Account type is required");
        }
        if (request.getInitialBalance() <= 0) {
            throw new ValidationException("Initial balance must be greater than 0");
        }
    }

    private boolean clientExists(Long clientId) {
        try {
            ResponseEntity<ClientResponse> response = restTemplate.getForEntity(
                    "http://localhost:8080/api/v1/clients/" + clientId,
                    ClientResponse.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }

    private String generateUniqueAccountNumber() {
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

