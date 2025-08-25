package banco;

import java.util.*;

public class Bank {
    private final Map<String, Cliente> clientesPorDni = new HashMap<>();
    private final Map<String, Account> cuentas = new HashMap<>();

    public boolean registrarCliente(Cliente c) {
        Objects.requireNonNull(c);
        if (clientesPorDni.containsKey(c.getDni())) return false;
        clientesPorDni.put(c.getDni(), c);
        return true;
    }

    public Cliente buscarCliente(String dni) { return clientesPorDni.get(dni); }

    public void agregarCuenta(Account acc) {
        Objects.requireNonNull(acc);
        if (cuentas.containsKey(acc.getNumero())) throw new IllegalArgumentException("Cuenta duplicada");
        cuentas.put(acc.getNumero(), acc);
    }

    public Account obtenerCuenta(String numero) { return cuentas.get(numero); }
    public Collection<Account> cuentas() { return Collections.unmodifiableCollection(cuentas.values()); }
    public Collection<Cliente> clientes() { return Collections.unmodifiableCollection(clientesPorDni.values()); }
}
