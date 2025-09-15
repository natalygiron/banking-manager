package com.bootcamp.clientms.dto;

import com.bootcamp.clientms.domain.ClientType;
import java.time.Instant;

public class ClientResponse {
  private Long id;
  private ClientType type;
  private String fullName;
  private String documentNumber;
  private String email;
  private String phone;
  private String address;
  private Instant createdAt;
  private Instant updatedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public ClientType getType() { return type; }
  public void setType(ClientType type) { this.type = type; }
  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }
  public String getDocumentNumber() { return documentNumber; }
  public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
