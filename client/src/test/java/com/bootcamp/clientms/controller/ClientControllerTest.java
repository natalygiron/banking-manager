package com.bootcamp.clientms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.domain.ClientType;
import com.bootcamp.clientms.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {

  @Autowired private MockMvc mvc;
  @MockBean private ClientService service;
  @Autowired private ObjectMapper om;

  private Client sample() {
    Client c = new Client();
    c.setId(1L);
    c.setType(ClientType.PERSON);
    c.setFullName("Juan Perez");
    c.setDocumentNumber("12345678");
    c.setEmail("juan@test.com");
    c.setPhone("999999999");
    c.setAddress("Lima");
    return c;
  }

  @Test
  void list_ok() throws Exception {
    when(service.list()).thenReturn(List.of(sample()));
    mvc.perform(get("/api/clients"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].documentNumber").value("12345678"));
  }

  @Test
  void getById_ok() throws Exception {
    when(service.getById(1L)).thenReturn(sample());
    mvc.perform(get("/api/clients/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.fullName").value("Juan Perez"));
  }
}
