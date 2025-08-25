package banco;

public class CheckingAccount extends Account {
    private final double sobregiroMaximo;

    public CheckingAccount(String numero, double saldoInicial, double sobregiroMaximo) {
        super(numero, saldoInicial);
        if (sobregiroMaximo < 0) throw new IllegalArgumentException("Sobregiro inválido");
        this.sobregiroMaximo = sobregiroMaximo;
    }

    @Override
    public void retirar(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("Monto inválido");
        if (saldo - monto < -sobregiroMaximo) {
            throw new IllegalArgumentException("Excede sobregiro permitido");
        }
        saldo -= monto;
    }

    public double getSobregiroMaximo() { return sobregiroMaximo; }
}
