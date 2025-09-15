package com.bootcamp.accountms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bootcamp.accountms.domain.Account;
import com.bootcamp.accountms.domain.AccountStatus;
import com.bootcamp.accountms.domain.AccountType;
import com.bootcamp.accountms.exception.BadRequestException;
import com.bootcamp.accountms.exception.NotFoundException;
import com.bootcamp.accountms.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AccountServiceImplTest {

  @Mock
  private AccountRepository repo;

  @InjectMocks
  private AccountServiceImpl service;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  private Account sample() {
    Account a = new Account();
    a.setId(1L);
    a.setAccountNumber("ACC-001");
    a.setType(AccountType.SAVINGS);
    a.setHolderName("Ana");
    a.setCurrency("PEN");
    a.setBalance(new BigDecimal("100.00"));
    a.setStatus(AccountStatus.ACTIVE);
    return a;
  }

  @Test
  void getById_ok() {
    when(repo.findById(1L)).thenReturn(Optional.of(sample()));
    Account a = service.getById(1L);
    assertEquals("ACC-001", a.getAccountNumber());
  }

  @Test
  void getById_notFound() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> service.getById(99L));
  }

  @Test
  void create_duplicateNumber() {
    Account a = sample();
    when(repo.existsByAccountNumber("ACC-001")).thenReturn(true);
    assertThrows(BadRequestException.class, () -> service.create(a));
  }

  @Test
  void deposit_ok() {
    Account a = sample();
    when(repo.findById(1L)).thenReturn(Optional.of(a));
    when(repo.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));
    Account out = service.deposit(1L, new BigDecimal("50.00"));
    assertEquals(new BigDecimal("150.00"), out.getBalance());
  }

  @Test
  void withdraw_insufficientFunds() {
    Account a = sample();
    when(repo.findById(1L)).thenReturn(Optional.of(a));
    assertThrows(BadRequestException.class, () -> service.withdraw(1L, new BigDecimal("200.00")));
  }
}
