package com.bootcamp.clientms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;

@Entity
@Table(name = "clients", uniqueConstraints = {
    @UniqueConstraint(columnNames = "documentNumber"),
    @UniqueConstraint(columnNames = "email")
})
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private ClientType type;

  @NotBlank
  @Column(nullable = false, length = 120)
  private String fullName;

  @NotBlank
  @Column(nullable = false, length = 20)
  private String documentNumber;

  @NotBlank
  @Email
  @Column(nullable = false, length = 120)
  private String email;

  @NotBlank
  @Column(nullable = false, length = 20)
  private String phone;

  @NotBlank
  @Column(nullable = false, length = 160)
  private String address;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;

  @PrePersist
  public void onCreate() { createdAt = Instant.now(); updatedAt = createdAt; }

  @PreUpdate
  public void onUpdate() { updatedAt = Instant.now(); }

  // Getters/setters
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
