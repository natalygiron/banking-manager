package com.bootcamp.clientms.service;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.exception.BadRequestException;
import com.bootcamp.clientms.exception.NotFoundException;
import com.bootcamp.clientms.repository.ClientRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

  private final ClientRepository repo;

  public ClientServiceImpl(ClientRepository repo) {
    this.repo = repo;
  }

  @Override
  public Client create(Client c) {
    if (repo.existsByDocumentNumber(c.getDocumentNumber())) {
      throw new BadRequestException("Document number already exists");
    }
    if (repo.existsByEmail(c.getEmail())) {
      throw new BadRequestException("Email already exists");
    }
    return repo.save(c);
  }

  @Override
  @Transactional(readOnly = true)
  public Client getById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public Client getByDocumentNumber(String documentNumber) {
    return repo.findByDocumentNumber(documentNumber)
        .orElseThrow(() -> new NotFoundException("Client not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public Client getByEmail(String email) {
    return repo.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Client not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Client> list() {
    return repo.findAll();
  }

  @Override
  public Client update(Long id, String fullName, String phone, String address) {
    Client c = getById(id);
    c.setFullName(fullName);
    c.setPhone(phone);
    c.setAddress(address);
    return repo.save(c);
  }

  @Override
  public void delete(Long id) {
    Client c = getById(id);
    repo.delete(c);
  }
}
