package com.banking.account.dto.request;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class WithdrawRequest {
    @Positive(message = "Amount must be greater than 0")
    private Double amount;
}
