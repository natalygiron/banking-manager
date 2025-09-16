package com.banking.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.banking.account.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByClientId(Long clientId);

}
