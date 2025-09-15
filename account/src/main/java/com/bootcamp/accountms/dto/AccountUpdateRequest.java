package com.bootcamp.accountms.dto;

import com.bootcamp.accountms.domain.AccountStatus;
import jakarta.validation.constraints.*;

public class AccountUpdateRequest {

  @NotBlank
  private String holderName;

  @NotNull
  private AccountStatus status;

  public String getHolderName() { return holderName; }
  public void setHolderName(String holderName) { this.holderName = holderName; }
  public AccountStatus getStatus() { return status; }
  public void setStatus(AccountStatus status) { this.status = status; }
}
