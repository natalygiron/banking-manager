package banco.service;

import banco.repository.BankRepository;
import banco.domain.AccountType;
import banco.domain.BankAccount;
import banco.domain.Client;

public class BankService {
    private final BankRepository repo;

    public BankService(BankRepository repo) {
        this.repo = repo;
    }

    public Client registerClient(String dni, String first, String last, String email) {
        return repo.registerClient(dni, first, last, email);
    }

    public BankAccount openAccount(String dni, AccountType type) {
        return repo.openAccount(dni, type);
    }

    public void deposit(String accNum, double amount) {
        repo.deposit(accNum, amount);
    }

    public void withdraw(String accNum, double amount) {
        repo.withdraw(accNum, amount);
    }

    public double getBalance(String accNum) {
        return repo.getBalance(accNum);
    }
}
