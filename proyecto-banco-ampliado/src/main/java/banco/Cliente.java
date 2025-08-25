package banco;

import java.util.Objects;

public class Cliente {
    private final String dni;
    private final String nombre;
    private final String email;

    public Cliente(String dni, String nombre, String email) {
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("DNI requerido");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre requerido");
        if (!EmailValidator.esEmailValido(email)) throw new IllegalArgumentException("Email inválido");
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente c = (Cliente)o;
        return dni.equals(c.dni);
    }
    @Override public int hashCode() { return Objects.hash(dni); }
    @Override public String toString() { return nombre + " (" + dni + ")"; }
}
