package com.bootcamp.accountms.service;

import com.bootcamp.accountms.domain.Account;
import com.bootcamp.accountms.domain.AccountStatus;
import com.bootcamp.accountms.exception.BadRequestException;
import com.bootcamp.accountms.exception.NotFoundException;
import com.bootcamp.accountms.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

  private final AccountRepository repo;

  public AccountServiceImpl(AccountRepository repo) {
    this.repo = repo;
  }

  @Override
  public Account create(Account a) {
    if (repo.existsByAccountNumber(a.getAccountNumber())) {
      throw new BadRequestException("Account number already exists");
    }
    return repo.save(a);
  }

  @Override
  @Transactional(readOnly = true)
  public Account getById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Account not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public Account getByAccountNumber(String accountNumber) {
    return repo.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new NotFoundException("Account not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Account> list() {
    return repo.findAll();
  }

  @Override
  public Account update(Long id, String holderName, AccountStatus status) {
    Account a = getById(id);
    a.setHolderName(holderName);
    a.setStatus(status);
    return repo.save(a);
  }

  @Override
  public void delete(Long id) {
    Account a = getById(id);
    repo.delete(a);
  }

  @Override
  public Account deposit(Long id, BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BadRequestException("Amount must be greater than zero");
    }
    Account a = getById(id);
    if (a.getStatus() != AccountStatus.ACTIVE) {
      throw new BadRequestException("Only ACTIVE accounts can receive deposits");
    }
    a.setBalance(a.getBalance().add(amount));
    return repo.save(a);
  }

  @Override
  public Account withdraw(Long id, BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BadRequestException("Amount must be greater than zero");
    }
    Account a = getById(id);
    if (a.getStatus() != AccountStatus.ACTIVE) {
      throw new BadRequestException("Only ACTIVE accounts can withdraw");
    }
    if (a.getBalance().compareTo(amount) < 0) {
      throw new BadRequestException("Insufficient funds");
    }
    a.setBalance(a.getBalance().subtract(amount));
    return repo.save(a);
  }
}
