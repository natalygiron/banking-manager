package com.banking.account.dto.response;

import com.banking.account.model.Account;
import com.banking.account.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private double balance;
    private AccountType type;
    private Long clientId;

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getType(),
                account.getClientId()
        );
    }
}
