package com.bootcamp.clientms.dto;

import com.bootcamp.clientms.domain.ClientType;
import jakarta.validation.constraints.*;

public class ClientCreateRequest {
  @NotNull private ClientType type;
  @NotBlank private String fullName;
  @NotBlank private String documentNumber;
  @NotBlank @Email private String email;
  @NotBlank private String phone;
  @NotBlank private String address;

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
}
