package banco;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CuentaTests {

    @Test
    void depositoYRetiroEnCheckingConSobregiro() {
        CheckingAccount c = new CheckingAccount("C-1", 100, 50);
        c.retirar(120); // queda -20 dentro del sobregiro
        assertEquals(-20, c.getSaldo(), 0.0001);
        c.depositar(50);
        assertEquals(30, c.getSaldo(), 0.0001);
        assertThrows(IllegalArgumentException.class, () -> c.retirar(100)); // excede sobregiro
    }

    @Test
    void ahorroNoPermiteNegativosYCapitaliza() {
        SavingsAccount s = new SavingsAccount("S-1", 200, 0.12); // 12% anual
        assertThrows(IllegalArgumentException.class, () -> s.retirar(250));
        s.capitalizarMensual();
        assertTrue(s.getSaldo() > 200);
    }
}
