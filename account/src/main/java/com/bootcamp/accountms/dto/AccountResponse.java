package com.bootcamp.accountms.dto;

import com.bootcamp.accountms.domain.AccountStatus;
import com.bootcamp.accountms.domain.AccountType;
import java.math.BigDecimal;
import java.time.Instant;

public class AccountResponse {
  private Long id;
  private String accountNumber;
  private AccountType type;
  private String holderName;
  private BigDecimal balance;
  private String currency;
  private AccountStatus status;
  private Instant createdAt;
  private Instant updatedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getAccountNumber() { return accountNumber; }
  public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
  public AccountType getType() { return type; }
  public void setType(AccountType type) { this.type = type; }
  public String getHolderName() { return holderName; }
  public void setHolderName(String holderName) { this.holderName = holderName; }
  public java.math.BigDecimal getBalance() { return balance; }
  public void setBalance(java.math.BigDecimal balance) { this.balance = balance; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
  public AccountStatus getStatus() { return status; }
  public void setStatus(AccountStatus status) { this.status = status; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
