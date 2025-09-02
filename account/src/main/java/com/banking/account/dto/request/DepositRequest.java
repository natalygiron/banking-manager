package com.banking.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {
    @Positive(message = "Amount must be greater than 0")
    private double amount;
}