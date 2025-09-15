package com.bootcamp.clientms.mapper;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.ClientResponse;

public final class ClientMapper {
  private ClientMapper() {}
  public static ClientResponse toResponse(Client c) {
    ClientResponse r = new ClientResponse();
    r.setId(c.getId());
    r.setType(c.getType());
    r.setFullName(c.getFullName());
    r.setDocumentNumber(c.getDocumentNumber());
    r.setEmail(c.getEmail());
    r.setPhone(c.getPhone());
    r.setAddress(c.getAddress());
    r.setCreatedAt(c.getCreatedAt());
    r.setUpdatedAt(c.getUpdatedAt());
    return r;
  }
}
