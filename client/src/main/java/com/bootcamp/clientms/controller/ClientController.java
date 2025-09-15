package com.bootcamp.clientms.controller;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.*;
import com.bootcamp.clientms.mapper.ClientMapper;
import com.bootcamp.clientms.service.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@Validated
public class ClientController {

  private final ClientService service;

  public ClientController(ClientService service) { this.service = service; }

  @PostMapping
  public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientCreateRequest req) {
    Client c = new Client();
    c.setType(req.getType());
    c.setFullName(req.getFullName());
    c.setDocumentNumber(req.getDocumentNumber());
    c.setEmail(req.getEmail());
    c.setPhone(req.getPhone());
    c.setAddress(req.getAddress());
    Client saved = service.create(c);
    return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
  }

  @GetMapping("/{id}")
  public ClientResponse getById(@PathVariable Long id) {
    return ClientMapper.toResponse(service.getById(id));
  }

  @GetMapping
  public List<ClientResponse> list(@RequestParam(value = "documentNumber", required = false) String documentNumber,
                                   @RequestParam(value = "email", required = false) String email) {
    if (documentNumber != null && !documentNumber.isBlank()) {
      return List.of(ClientMapper.toResponse(service.getByDocumentNumber(documentNumber)));
    }
    if (email != null && !email.isBlank()) {
      return List.of(ClientMapper.toResponse(service.getByEmail(email)));
    }
    return service.list().stream().map(ClientMapper::toResponse).collect(Collectors.toList());
  }

  @PutMapping("/{id}")
  public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientUpdateRequest req) {
    return ClientMapper.toResponse(service.update(id, req.getFullName(), req.getPhone(), req.getAddress()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) { service.delete(id); }
}
