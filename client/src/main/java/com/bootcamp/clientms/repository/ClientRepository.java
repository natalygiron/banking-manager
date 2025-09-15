package com.bootcamp.clientms.repository;

import com.bootcamp.clientms.domain.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
  Optional<Client> findByDocumentNumber(String documentNumber);
  Optional<Client> findByEmail(String email);
  boolean existsByDocumentNumber(String documentNumber);
  boolean existsByEmail(String email);
}
