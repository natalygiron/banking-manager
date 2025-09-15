package com.bootcamp.clientms.service;

import com.bootcamp.clientms.domain.Client;
import java.util.List;

public interface ClientService {
  Client create(Client c);
  Client getById(Long id);
  Client getByDocumentNumber(String documentNumber);
  Client getByEmail(String email);
  List<Client> list();
  Client update(Long id, String fullName, String phone, String address);
  void delete(Long id);
}
