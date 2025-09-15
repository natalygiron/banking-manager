package com.bootcamp.accountms.dto;

import com.bootcamp.accountms.domain.AccountType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AccountCreateRequest {

  @NotBlank
  private String accountNumber;

  @NotNull
  private AccountType type;

  @NotBlank
  private String holderName;

  @NotNull
  @DecimalMin("0.00")
  private BigDecimal initialBalance;

  @NotBlank
  private String currency;

  public String getAccountNumber() { return accountNumber; }
  public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
  public AccountType getType() { return type; }
  public void setType(AccountType type) { this.type = type; }
  public String getHolderName() { return holderName; }
  public void setHolderName(String holderName) { this.holderName = holderName; }
  public BigDecimal getInitialBalance() { return initialBalance; }
  public void setInitialBalance(BigDecimal initialBalance) { this.initialBalance = initialBalance; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
}
