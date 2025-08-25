package banco.repository;

import banco.domain.AccountType;
import banco.domain.BankAccount;
import banco.domain.Client;
import banco.util.IdGenerator;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class BankRepository {
    private final Map<String, Client> clientsByDni = new HashMap<>();
    private final Map<String, BankAccount> accountsByNumber = new HashMap<>();
    private final AtomicLong accountSeq = new AtomicLong(1);

    public Client registerClient(String dni, String firstName, String lastName, String email) {
        if (clientsByDni.containsKey(dni)) throw new IllegalArgumentException("DNI ya registrado");
        Client c = new Client(dni, firstName, lastName, email);
        clientsByDni.put(dni, c);
        return c;
    }

    public Optional<Client> getClient(String dni) {
        return Optional.ofNullable(clientsByDni.get(dni));
    }

    public BankAccount openAccount(String dni, AccountType type) {
        Client c = clientsByDni.get(dni);
        if (c == null) throw new NoSuchElementException("Cliente no existe");

        String accNumber = IdGenerator.accountNumber("ACC");
        if (accountsByNumber.containsKey(accNumber)) throw new IllegalStateException("Conflicto número de cuenta");

        BankAccount account = new BankAccount(
                accNumber,
                0.0,
                type,
                type == AccountType.CHECKING ? BankAccount.DEFAULT_OVERDRAFT_LIMIT : 0.0
        );

        c.addAccount(account);
        accountsByNumber.put(accNumber, account);
        return account;
    }

    public void deposit(String accountNumber, double amount) {
        account(accountNumber).deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) {
        account(accountNumber).withdraw(amount);
    }

    public double getBalance(String accountNumber) {
        return account(accountNumber).getBalance();
    }

    public Optional<BankAccount> getAccount(String accountNumber) {
        return Optional.ofNullable(accountsByNumber.get(accountNumber));
    }

    public List<BankAccount> listAccountsByClient(String dni) {
        Client c = clientsByDni.get(dni);
        if (c == null) throw new NoSuchElementException("Cliente no existe");
        return c.getAccounts();
    }

    private BankAccount account(String number) {
        BankAccount a = accountsByNumber.get(number);
        if (a == null) throw new NoSuchElementException("Cuenta no existe");
        return a;
    }

    private String nextAccountNumber() {
        long n = accountSeq.getAndIncrement();
        return String.format("ACC-%06d", n);
    }
}
