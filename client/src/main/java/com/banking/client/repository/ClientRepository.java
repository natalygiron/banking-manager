package com.banking.client.repository;

import com.banking.client.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}
