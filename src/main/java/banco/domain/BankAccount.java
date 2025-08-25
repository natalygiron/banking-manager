package banco.domain;

public class BankAccount {
    public static final double DEFAULT_OVERDRAFT_LIMIT = 500.0;

    private final String accountNumber;
    private double balance;
    private final AccountType type;
    private final double overdraftLimit;

    public BankAccount(String accountNumber, AccountType type) {
        this(accountNumber, 0.0, type,
                type == AccountType.CHECKING ? DEFAULT_OVERDRAFT_LIMIT : 0.0);
    }

    public BankAccount(String accountNumber, double initialBalance,
                       AccountType type, double overdraftLimit) {
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number required");
        if (type == null)
            throw new IllegalArgumentException("Account type required");

        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.type = type;
        this.overdraftLimit = (type == AccountType.CHECKING) ? overdraftLimit : 0.0;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");

        if (type == AccountType.SAVINGS && balance - amount < 0) {
            throw new IllegalArgumentException("Insufficient funds - savings cannot be negative");
        }
        if (type == AccountType.CHECKING && balance - amount < -overdraftLimit) {
            throw new IllegalArgumentException("Exceeds overdraft limit");
        }
        balance -= amount;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public AccountType getType() { return type; }
    public double getOverdraftLimit() { return overdraftLimit; }
}
