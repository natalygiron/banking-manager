package banco;

public class SavingsAccount extends Account {
    private final double tasaAnual; // 0.05 = 5%

    public SavingsAccount(String numero, double saldoInicial, double tasaAnual) {
        super(numero, saldoInicial);
        if (tasaAnual < 0) throw new IllegalArgumentException("Tasa inválida");
        this.tasaAnual = tasaAnual;
    }

    @Override
    public void retirar(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("Monto inválido");
        if (saldo - monto < 0) throw new IllegalArgumentException("Ahorro no permite saldo negativo");
        saldo -= monto;
    }

    public void capitalizarMensual() {
        saldo += saldo * (tasaAnual / 12.0);
    }

    public double getTasaAnual() { return tasaAnual; }
}
