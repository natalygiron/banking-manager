package com.bootcamp.clientms.dto;

import jakarta.validation.constraints.*;

public class ClientUpdateRequest {
  @NotBlank private String fullName;
  @NotBlank private String phone;
  @NotBlank private String address;

  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
}
