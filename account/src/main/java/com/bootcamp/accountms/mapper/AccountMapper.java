package com.bootcamp.accountms.mapper;

import com.bootcamp.accountms.domain.Account;
import com.bootcamp.accountms.dto.AccountResponse;

public final class AccountMapper {

  private AccountMapper() {}

  public static AccountResponse toResponse(Account a) {
    AccountResponse r = new AccountResponse();
    r.setId(a.getId());
    r.setAccountNumber(a.getAccountNumber());
    r.setType(a.getType());
    r.setHolderName(a.getHolderName());
    r.setBalance(a.getBalance());
    r.setCurrency(a.getCurrency());
    r.setStatus(a.getStatus());
    r.setCreatedAt(a.getCreatedAt());
    r.setUpdatedAt(a.getUpdatedAt());
    return r;
  }
}
