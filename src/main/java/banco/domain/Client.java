package banco.domain;

import banco.util.EmailValidator;

import java.util.*;
import java.util.stream.Collectors;

public class Client {
    private final String dni;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<BankAccount> accounts = new ArrayList<>();

    public Client(String dni, String firstName, String lastName, String email) {
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("DNI requerido");
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("Nombre requerido");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Apellido requerido");
        if (!EmailValidator.isValid(email)) throw new IllegalArgumentException("Email inválido");
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addAccount(BankAccount account) {
        if (account == null) throw new IllegalArgumentException("Cuenta requerida");
        String acc = account.getAccountNumber();
        if (acc == null || acc.isBlank()) throw new IllegalArgumentException("Número de cuenta requerido");
        if (hasAccountNumber(acc)) throw new IllegalArgumentException("El cliente ya tiene la cuenta " + acc);
        accounts.add(account);
    }

    public boolean removeAccount(BankAccount account) {
        return account != null && accounts.remove(account);
    }

    public boolean removeAccountByNumber(String accountNumber) {
        return accountNumber != null && accounts.removeIf(a -> accountNumber.equals(a.getAccountNumber()));
    }

    public List<BankAccount> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public Optional<BankAccount> getAccountByNumber(String accountNumber) {
        if (accountNumber == null) return Optional.empty();
        return accounts.stream().filter(a -> accountNumber.equals(a.getAccountNumber())).findFirst();
    }

    public boolean hasAccountNumber(String accountNumber) {
        return accountNumber != null && accounts.stream().anyMatch(a -> accountNumber.equals(a.getAccountNumber()));
    }

    public String getDni() { return dni; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client c = (Client) o;
        return dni.equals(c.dni);
    }

    @Override public int hashCode() { return Objects.hash(dni); }

    @Override public String toString() {
        String accs = accounts.stream()
                .map(a -> a.getAccountNumber() + ":" + a.getType() + " (bal=" + a.getBalance() + ")")
                .collect(Collectors.joining(", "));
        return firstName + " " + lastName + " (" + dni + ") - accounts[" + accs + "]";
    }
}
