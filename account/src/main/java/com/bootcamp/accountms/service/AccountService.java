package com.bootcamp.accountms.service;

import com.bootcamp.accountms.domain.Account;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
  Account create(Account a);
  Account getById(Long id);
  Account getByAccountNumber(String accountNumber);
  List<Account> list();
  Account update(Long id, String holderName, com.bootcamp.accountms.domain.AccountStatus status);
  void delete(Long id);
  Account deposit(Long id, BigDecimal amount);
  Account withdraw(Long id, BigDecimal amount);
}
