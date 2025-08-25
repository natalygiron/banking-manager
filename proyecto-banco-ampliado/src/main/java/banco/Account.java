package banco;

public abstract class Account {
    protected final String numero;
    protected double saldo;

    protected Account(String numero, double saldoInicial) {
        if (numero == null || numero.isBlank()) throw new IllegalArgumentException("Número requerido");
        this.numero = numero;
        this.saldo = saldoInicial;
    }

    public void depositar(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("Monto inválido");
        saldo += monto;
    }

    public abstract void retirar(double monto);

    public String getNumero() { return numero; }
    public double getSaldo() { return saldo; }
}
