package com.banking.client.service;

import com.banking.client.domain.Client;
import com.banking.client.dto.request.CreateClientRequest;
import com.banking.client.port.AccountsClient;
import com.banking.client.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    ClientRepository clientRepository;

    @Mock
    AccountsClient accountsClient;

    @InjectMocks
    ClientService service;

    // ---------- register() ----------
    @Test
    void register_ok() {
        CreateClientRequest req = new CreateClientRequest();
        req.setFirstName("Ana");
        req.setLastName("Perez");
        req.setDni("12345678");
        req.setEmail("ana@mail.com");

        when(clientRepository.existsByDni("12345678")).thenReturn(false);
        when(clientRepository.existsByEmail("ana@mail.com")).thenReturn(false);
        Client saved = Client.builder()
                .id(1L).firstName("Ana").lastName("Perez")
                .dni("12345678").email("ana@mail.com")
                .build();
        when(clientRepository.save(any(Client.class))).thenReturn(saved);

        Client out = service.register(req);

        assertEquals(1L, out.getId());
        verify(clientRepository).existsByDni("12345678");
        verify(clientRepository).existsByEmail("ana@mail.com");
        verify(clientRepository).save(any(Client.class));
        verifyNoMoreInteractions(clientRepository, accountsClient);
    }

    @Test
    void register_fails_when_required_fields_blank() {
        CreateClientRequest req = new CreateClientRequest();
        var ex = assertThrows(ValidationException.class, () -> service.register(req));
        assertTrue(ex.getMessage().contains("All fields are required"));
        verifyNoInteractions(clientRepository, accountsClient);
    }

    @Test
    void register_fails_when_dni_duplicated() {
        CreateClientRequest req = new CreateClientRequest();
        req.setFirstName("Ana"); req.setLastName("Perez");
        req.setDni("123"); req.setEmail("a@a.com");

        when(clientRepository.existsByDni("123")).thenReturn(true);

        var ex = assertThrows(IllegalArgumentException.class, () -> service.register(req));
        assertTrue(ex.getMessage().contains("Dni is already in use"));
        verify(clientRepository).existsByDni("123");
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    @Test
    void register_fails_when_email_duplicated() {
        CreateClientRequest req = new CreateClientRequest();
        req.setFirstName("Ana"); req.setLastName("Perez");
        req.setDni("123"); req.setEmail("a@a.com");

        when(clientRepository.existsByDni("123")).thenReturn(false);
        when(clientRepository.existsByEmail("a@a.com")).thenReturn(true);

        var ex = assertThrows(IllegalArgumentException.class, () -> service.register(req));
        assertTrue(ex.getMessage().contains("Email is already in use"));
        verify(clientRepository).existsByDni("123");
        verify(clientRepository).existsByEmail("a@a.com");
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    // ---------- get() ----------
    @Test
    void get_found() {
        Client c = Client.builder().id(7L).firstName("Ana").build();
        when(clientRepository.findById(7L)).thenReturn(Optional.of(c));

        Client out = service.get(7L);

        assertEquals(7L, out.getId());
        verify(clientRepository).findById(7L);
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    @Test
    void get_not_found() {
        when(clientRepository.findById(7L)).thenReturn(Optional.empty());
        var ex = assertThrows(IllegalArgumentException.class, () -> service.get(7L));
        assertTrue(ex.getMessage().contains("Client not found"));
        verify(clientRepository).findById(7L);
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    // ---------- list() ----------
    @Test
    void list_ok() {
        when(clientRepository.findAll()).thenReturn(List.of(
                Client.builder().id(1L).build(),
                Client.builder().id(2L).build()
        ));

        var list = service.list();
        assertEquals(2, list.size());
        verify(clientRepository).findAll();
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    // ---------- updateClient() ----------
    @Test
    void updateClient_ok_with_new_email_not_duplicated() {
        Client existing = Client.builder()
                .id(1L).firstName("Ana").lastName("P")
                .email("old@mail.com").build();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        Client out = service.updateClient(1L, "Ana María", "Perez", "new@mail.com");

        assertEquals("Ana María", out.getFirstName());
        assertEquals("Perez", out.getLastName());
        assertEquals("new@mail.com", out.getEmail());
        verify(clientRepository).findById(1L);
        verify(clientRepository).existsByEmail("new@mail.com");
        verify(clientRepository).save(existing);
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    @Test
    void updateClient_email_duplicated_throws() {
        Client existing = Client.builder()
                .id(1L).firstName("Ana").lastName("P")
                .email("old@mail.com").build();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.existsByEmail("dup@mail.com")).thenReturn(true);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateClient(1L, "Ana", "P", "dup@mail.com"));
        assertTrue(ex.getMessage().contains("Email is already in use"));
        verify(clientRepository).findById(1L);
        verify(clientRepository).existsByEmail("dup@mail.com");
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    @Test
    void updateClient_not_found_throws() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateClient(99L, "A", "B", "c@d.com"));
        assertTrue(ex.getMessage().contains("not found"));
        verify(clientRepository).findById(99L);
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    // ---------- deleteClient() ----------
    @Test
    void deleteClient_ok_when_no_accounts() {
        when(clientRepository.existsById(10L)).thenReturn(true);
        when(accountsClient.hasAccounts(10L)).thenReturn(false);

        service.deleteClient(10L);

        verify(clientRepository).existsById(10L);
        verify(accountsClient).hasAccounts(10L);
        verify(clientRepository).deleteById(10L);
        verifyNoMoreInteractions(clientRepository, accountsClient);
    }

    @Test
    void deleteClient_not_found_throws() {
        when(clientRepository.existsById(10L)).thenReturn(false);
        var ex = assertThrows(IllegalArgumentException.class, () -> service.deleteClient(10L));
        assertTrue(ex.getMessage().contains("Client not found"));
        verify(clientRepository).existsById(10L);
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(accountsClient);
    }

    @Test
    void deleteClient_with_active_accounts_throws() {
        when(clientRepository.existsById(10L)).thenReturn(true);
        when(accountsClient.hasAccounts(10L)).thenReturn(true);

        var ex = assertThrows(ValidationException.class, () -> service.deleteClient(10L));
        assertTrue(ex.getMessage().contains("Cannot delete client with active accounts"));
        verify(clientRepository).existsById(10L);
        verify(accountsClient).hasAccounts(10L);
        verifyNoMoreInteractions(clientRepository, accountsClient);
    }
}
