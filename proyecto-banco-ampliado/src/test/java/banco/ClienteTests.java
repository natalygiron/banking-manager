package banco;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteTests {

    @Test
    void creaClienteOk() {
        Cliente c = new Cliente("12345678", "Ana", "ana@test.com");
        assertEquals("12345678", c.getDni());
        assertEquals("Ana", c.getNombre());
        assertEquals("ana@test.com", c.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "ana@", "@dom.com", "ana@dom", " ana@dom.com "})
    void emailInvalido(String email) {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("99999999", "Ana", email));
    }
}
