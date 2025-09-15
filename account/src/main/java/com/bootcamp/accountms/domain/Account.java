package com.bootcamp.accountms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(columnNames = "accountNumber"))
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false, length = 30)
  private String accountNumber;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private AccountType type;

  @NotBlank
  @Column(nullable = false, length = 120)
  private String holderName;

  @NotNull
  @DecimalMin(value = "0.00")
  @Column(nullable = false, precision = 18, scale = 2)
  private BigDecimal balance = BigDecimal.ZERO;

  @NotBlank
  @Column(nullable = false, length = 3)
  private String currency; // e.g., PEN, USD

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private AccountStatus status = AccountStatus.ACTIVE;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;

  @PrePersist
  public void onCreate() {
    createdAt = Instant.now();
    updatedAt = createdAt;
  }

  @PreUpdate
  public void onUpdate() {
    updatedAt = Instant.now();
  }

  // Getters & setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getAccountNumber() { return accountNumber; }
  public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
  public AccountType getType() { return type; }
  public void setType(AccountType type) { this.type = type; }
  public String getHolderName() { return holderName; }
  public void setHolderName(String holderName) { this.holderName = holderName; }
  public BigDecimal getBalance() { return balance; }
  public void setBalance(BigDecimal balance) { this.balance = balance; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
  public AccountStatus getStatus() { return status; }
  public void setStatus(AccountStatus status) { this.status = status; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
