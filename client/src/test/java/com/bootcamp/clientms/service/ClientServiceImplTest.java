package com.bootcamp.clientms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.domain.ClientType;
import com.bootcamp.clientms.exception.BadRequestException;
import com.bootcamp.clientms.exception.NotFoundException;
import com.bootcamp.clientms.repository.ClientRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ClientServiceImplTest {

  @Mock private ClientRepository repo;
  @InjectMocks private ClientServiceImpl service;

  @BeforeEach
  void setup() { MockitoAnnotations.openMocks(this); }

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
  void getById_ok() {
    when(repo.findById(1L)).thenReturn(Optional.of(sample()));
    Client c = service.getById(1L);
    assertEquals("Juan Perez", c.getFullName());
  }

  @Test
  void getById_notFound() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> service.getById(99L));
  }

  @Test
  void create_duplicates() {
    Client c = sample();
    when(repo.existsByDocumentNumber("12345678")).thenReturn(false);
    when(repo.existsByEmail("juan@test.com")).thenReturn(true);
    assertThrows(BadRequestException.class, () -> service.create(c));
  }
}
