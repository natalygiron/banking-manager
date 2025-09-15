package com.bootcamp.accountms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bootcamp.accountms.domain.Account;
import com.bootcamp.accountms.domain.AccountStatus;
import com.bootcamp.accountms.domain.AccountType;
import com.bootcamp.accountms.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private AccountService service;

  @Autowired
  private ObjectMapper om;

  private Account sample() {
    Account a = new Account();
    a.setId(1L);
    a.setAccountNumber("ACC-001");
    a.setType(AccountType.SAVINGS);
    a.setHolderName("Ana");
    a.setCurrency("PEN");
    a.setBalance(new BigDecimal("100.00"));
    a.setStatus(AccountStatus.ACTIVE);
    return a;
  }

  @Test
  void list_ok() throws Exception {
    when(service.list()).thenReturn(List.of(sample()));
    mvc.perform(get("/api/accounts"))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$[0].accountNumber").value("ACC-001"));
  }

  @Test
  void getById_ok() throws Exception {
    when(service.getById(1L)).thenReturn(sample());
    mvc.perform(get("/api/accounts/1"))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.holderName").value("Ana"));
  }
}
