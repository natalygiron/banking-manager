package com.banking.account.service;

import com.banking.account.model.Account;
import com.banking.account.repository.AccountRepository;
import com.banking.account.dto.request.CreateAccountRequest;
import com.banking.account.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountValidator validator;
    private final ClientGateway clientGateway;
    private final AccountNumberGenerator accountNumberGenerator;

    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        validator.validateRequest(request);

        if (!clientGateway.exists(request.getClientId())) {
            throw new IllegalArgumentException("Client with ID " + request.getClientId() + " does not exist");
        }

        String accountNumber = accountNumberGenerator.generate();

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

    @Transactional
    public Account deposit(Long accountId, double amount) {
        validator.validateAmount(amount);
        Account account = get(accountId);
        account.deposit(amount);
        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(Long accountId, double amount) {
        validator.validateAmount(amount);
        Account account = get(accountId);
        account.withdraw(amount);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account get(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Transactional(readOnly = true)
    public List<Account> listAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Account> listByClient(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    @Transactional
    public void delete(Long accountId) {
        Account account = get(accountId);
        if (account.getBalance() != 0.0) {
            throw new ValidationException("Cannot delete account with non-zero balance");
        }
        accountRepository.delete(account);
        log.info("Account deleted: {}", account.getAccountNumber());
    }
}
