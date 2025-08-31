package com.banking.account;

import com.banking.account.dto.request.CreateAccountRequest;
import com.banking.account.dto.response.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> open(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request);
        log.info("New account registration {}", request);
        return ResponseEntity.ok(AccountResponse.from(account));
    }
}
