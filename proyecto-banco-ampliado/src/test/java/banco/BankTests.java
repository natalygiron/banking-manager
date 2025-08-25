package banco;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankTests {

    @Test
    void registrarYBuscarCliente() {
        Bank b = new Bank();
        Cliente c = new Cliente("11111111", "Juan", "juan@test.com");
        assertTrue(b.registrarCliente(c));
        assertFalse(b.registrarCliente(c)); // DNI duplicado
        assertEquals("Juan", b.buscarCliente("11111111").getNombre());
    }

    @Test
    void agregarCuentaDuplicadaLanzaError() {
        Bank b = new Bank();
        CheckingAccount c = new CheckingAccount("C-1", 0, 100);
        b.agregarCuenta(c);
        assertThrows(IllegalArgumentException.class, () -> b.agregarCuenta(new CheckingAccount("C-1", 0, 100)));
    }
}
