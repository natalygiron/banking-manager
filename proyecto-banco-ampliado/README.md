# Proyecto Banco (VS Code + Maven + JUnit 5)

Proyecto de ejemplo con clases `Cliente`, `CheckingAccount`, `SavingsAccount` y `Bank`, acompañado de **pruebas JUnit 5**.

## Requisitos
- Java 11+
- Maven 3.8+
- VS Code con extensiones: *Extension Pack for Java*, *Test Runner for Java*, *Maven for Java*

## Ejecutar pruebas
```bash
mvn test
```
Se genera reporte de cobertura en `target/site/jacoco/index.html`.

## Estructura
```
src/
  main/java/banco/
    Account.java
    CheckingAccount.java
    SavingsAccount.java
    Cliente.java
    EmailValidator.java
    Bank.java
  test/java/banco/
    ClienteTests.java
    CuentaTests.java
    BankTests.java
```

## Notas
- `CheckingAccount` permite sobregiro hasta un máximo configurado.
- `SavingsAccount` no permite saldo negativo y puede capitalizar interés mensual.
- `Bank` asegura DNIs únicos y evita cuentas duplicadas.
```
