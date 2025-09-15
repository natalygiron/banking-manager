package com.bootcamp.accountms.controller;

import com.bootcamp.accountms.domain.Account;
import com.bootcamp.accountms.domain.AccountStatus;
import com.bootcamp.accountms.dto.*;
import com.bootcamp.accountms.mapper.AccountMapper;
import com.bootcamp.accountms.service.AccountService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Validated
public class AccountController {

  private final AccountService service;

  public AccountController(AccountService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountCreateRequest req) {
    Account a = new Account();
    a.setAccountNumber(req.getAccountNumber());
    a.setType(req.getType());
    a.setHolderName(req.getHolderName());
    a.setBalance(req.getInitialBalance());
    a.setCurrency(req.getCurrency());
    Account saved = service.create(a);
    return ResponseEntity.status(HttpStatus.CREATED).body(AccountMapper.toResponse(saved));
  }

  @GetMapping("/{id}")
  public AccountResponse getById(@PathVariable Long id) {
    return AccountMapper.toResponse(service.getById(id));
  }

  @GetMapping
  public List<AccountResponse> list(@RequestParam(value = "accountNumber", required = false) String accountNumber) {
    if (accountNumber != null && !accountNumber.isBlank()) {
      return List.of(AccountMapper.toResponse(service.getByAccountNumber(accountNumber)));
    }
    return service.list().stream().map(AccountMapper::toResponse).collect(Collectors.toList());
  }

  @PutMapping("/{id}")
  public AccountResponse update(@PathVariable Long id, @Valid @RequestBody AccountUpdateRequest req) {
    return AccountMapper.toResponse(service.update(id, req.getHolderName(), req.getStatus()));
  }

  @PatchMapping("/{id}/deposit")
  public AccountResponse deposit(@PathVariable Long id, @Valid @RequestBody AmountRequest req) {
    return AccountMapper.toResponse(service.deposit(id, req.getAmount()));
  }

  @PatchMapping("/{id}/withdraw")
  public AccountResponse withdraw(@PathVariable Long id, @Valid @RequestBody AmountRequest req) {
    return AccountMapper.toResponse(service.withdraw(id, req.getAmount()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
