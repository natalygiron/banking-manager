package com.banking.account;

import com.banking.account.dto.request.CreateAccountRequest;
import com.banking.account.dto.request.DepositRequest;
import com.banking.account.dto.request.WithdrawRequest;
import com.banking.account.dto.response.AccountResponse;
import com.banking.account.dto.response.BalanceResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("cuentas")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> open(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request);
        log.info("New account registration {}", request);
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PutMapping("/{id}/depositar")
    public ResponseEntity<BalanceResponse> deposit(@PathVariable Long id,
                                                   @Valid @RequestBody DepositRequest request) {
        Account account = accountService.deposit(id, request.getAmount());
        log.info("New deposit made {}", request);
        return ResponseEntity.ok(new BalanceResponse(account.getId(), account.getBalance()));
    }

    @PutMapping("/{id}/retirar")
    public ResponseEntity<BalanceResponse> withdraw(@PathVariable Long id,
                                                    @Valid @RequestBody WithdrawRequest request) {
        Account account = accountService.withdraw(id, request.getAmount());
        log.info("New withdraw made {}", request);
        return ResponseEntity.ok(new BalanceResponse(account.getId(), account.getBalance()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<AccountResponse>> getByClient(@PathVariable Long id) {
        List<AccountResponse> accounts = accountService.listByClient(id)
                .stream().map(AccountResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> list() {
        return ResponseEntity.ok(
                accountService.listAll().stream().map(AccountResponse::from).collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
