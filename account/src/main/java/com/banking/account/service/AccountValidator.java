package com.banking.account.service;

import javax.validation.ValidationException;
import org.springframework.stereotype.Component;
import com.banking.account.dto.request.CreateAccountRequest;

// Validación
@Component
public class AccountValidator {
    public void validateAmount(double amount) {
        if (amount <= 0) throw new ValidationException("Amount must be greater than 0");
    }

    public void validateRequest(CreateAccountRequest request) {
        if (request.getType() == null) throw new ValidationException("Account type is required");
        if (request.getInitialBalance() <= 0) throw new ValidationException("Initial balance must be greater than 0");
    }
}
