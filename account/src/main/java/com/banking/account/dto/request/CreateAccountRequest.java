package com.banking.account.dto.request;

import com.banking.account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    @NotNull private Long clientId;
    @NotNull private AccountType type;
    @Positive(message = "Initial balance must be greater than 0")
    private double initialBalance;
}


