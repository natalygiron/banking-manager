package banco;

import banco.domain.AccountType;
import banco.domain.BankAccount;
import banco.domain.Client;
import banco.repository.BankRepository;
import banco.service.BankService;

public class Main {
    public static void main(String[] args) {
        BankRepository repo = new BankRepository();   // En memoria
        BankService svc = new BankService(repo);

        try {
            // 1) Registrar cliente
            System.out.println("== REGISTRAR CLIENTE ==");
            Client c = svc.registerClient("12345678", "Ana", "Pérez", "ana@example.com");
            System.out.println("Cliente: " + c.getFullName() + " | DNI: " + c.getDni());

            // 2) Abrir cuentas
            System.out.println("\n== ABRIR CUENTAS ==");
            BankAccount savings  = svc.openAccount(c.getDni(), AccountType.SAVINGS);
            BankAccount checking = svc.openAccount(c.getDni(), AccountType.CHECKING);
            System.out.println("SAVINGS:  " + savings.getAccountNumber());
            System.out.println("CHECKING: " + checking.getAccountNumber());

            // 3) Operaciones en SAVINGS
            System.out.println("\n== OPERACIONES SAVINGS ==");
            svc.deposit(savings.getAccountNumber(), 500.0);
            System.out.println("Depósito 500 -> saldo: " + svc.getBalance(savings.getAccountNumber()));
            svc.withdraw(savings.getAccountNumber(), 120.0);
            System.out.println("Retiro 120  -> saldo: " + svc.getBalance(savings.getAccountNumber()));

            // 4) Sobregiro en CHECKING
            System.out.println("\n== PRUEBAS CHECKING (OVERDRAFT) ==");
            // saldo inicial 0. Límite por defecto = 500 (puede ser distinto si lo cambiaste)
            System.out.println("Saldo inicial checking: " + svc.getBalance(checking.getAccountNumber()));

            // 4.1 Retiro válido dentro del sobregiro (por ejemplo, -200)
            svc.withdraw(checking.getAccountNumber(), 200.0);
            System.out.println("Retiro 200 (válido). Saldo: " + svc.getBalance(checking.getAccountNumber()));

            // 4.2 Intento de exceder el sobregiro (por ejemplo, llevar a -600 si el límite es -500)
            try {
                svc.withdraw(checking.getAccountNumber(), 400.0); // esto intentaría pasar de -200 a -600
                System.out.println("¡Esto no debería imprimirse si el límite es 500!");
            } catch (IllegalArgumentException ex) {
                System.out.println("Retiro 400 (excede límite) → ERROR esperado: " + ex.getMessage());
            }

            //CASO NOMBRE NO INGRESADO
            //Client a = svc.registerClient("12345678", "", "Pérez", "a@example.com");
            //CASO APELLIDO NO INGRESADO
            //Client b = svc.registerClient("12345679", "Cesar", "", "b@example.com");
            //CASO CORREO INCORRECTO
            Client d = svc.registerClient("12345671", "Ramon", "Perez", "dd.com");

            System.out.println("\n== PRUEBA COMPLETA ✅ ==");

        } catch (Exception e) {
            System.out.println("✖ Error durante la prueba: " + e.getMessage());
        }
    }
}
