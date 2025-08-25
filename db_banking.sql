-- Crear base de datos banking si no existe
DROP DATABASE IF EXISTS banking;
CREATE DATABASE banking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE banking;


CREATE TABLE customer (
                          id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                          first_name VARCHAR(80) NOT NULL,
                          last_name VARCHAR(80) NOT NULL,
                          dni VARCHAR(16) NOT NULL,
                          email VARCHAR(120) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_customer_dni (dni),
                          UNIQUE KEY uk_customer_email (email),
                          INDEX idx_customer_name (first_name, last_name)
) ENGINE=InnoDB;


CREATE TABLE bank_account (
                              account_number VARCHAR(40) NOT NULL,
                              type ENUM('SAVINGS','CHECKING') NOT NULL,
                              balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                              overdraft_limit DECIMAL(15,2) DEFAULT 0.00,
                              customer_id BIGINT UNSIGNED NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              PRIMARY KEY (account_number),
                              CONSTRAINT fk_account_customer_id FOREIGN KEY (customer_id)
                                  REFERENCES customer(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                              CONSTRAINT chk_balance_overdraft CHECK (
                                  (type = 'SAVINGS' AND balance >= 0) OR
                                  (type = 'CHECKING' AND balance >= -overdraft_limit)
                                  ),
                              INDEX idx_account_customer (customer_id),
                              INDEX idx_account_type (type)
) ENGINE=InnoDB;

-- DATOS DE PRUEBA

-- Insertar clientes de prueba
INSERT INTO customer(first_name, last_name, dni, email) VALUES
                                                            ('Ana', 'Pérez', '12345678', 'ana.perez@email.com'),
                                                            ('Juan', 'García', '87654321', 'juan.garcia@email.com'),
                                                            ('María', 'López', '11223344', 'maria.lopez@email.com');

-- Insertar cuentas de prueba
INSERT INTO bank_account(account_number, customer_id, type, balance, overdraft_limit) VALUES
                                                                                          ('ACC-000001', 1, 'SAVINGS', 1000.00, 0.00),
                                                                                          ('ACC-000002', 1, 'CHECKING', 250.00, 500.00),
                                                                                          ('ACC-000003', 2, 'SAVINGS', 750.50, 0.00),
                                                                                          ('ACC-000004', 3, 'CHECKING', -100.00, 500.00);


-- CONSULTAS DE VERIFICACIÓN


-- 1. Consultar todos los clientes
SELECT
    id,
    first_name,
    last_name,
    dni,
    email,
    created_at
FROM customer
ORDER BY created_at;

-- 2. Consultar todas las cuentas con información del cliente
SELECT
    ba.account_number,
    ba.type,
    ba.balance,
    ba.overdraft_limit,
    CONCAT(c.first_name, ' ', c.last_name) as customer_name,
    c.dni as customer_dni
FROM bank_account ba
         JOIN customer c ON ba.customer_id = c.id
ORDER BY ba.account_number;

-- 3. Consultar cuentas por cliente específico
SELECT
    ba.account_number,
    ba.type,
    ba.balance,
    ba.overdraft_limit
FROM bank_account ba
         JOIN customer c ON ba.customer_id = c.id
WHERE c.dni = '12345678';


-- OPERACIONES DE PRUEBA


-- 4. Realizar un depósito
UPDATE bank_account
SET balance = balance + 500.00, updated_at = CURRENT_TIMESTAMP
WHERE account_number = 'ACC-000001';

-- Verificar el depósito
SELECT account_number, type, balance, updated_at
FROM bank_account
WHERE account_number = 'ACC-000001';

-- 5. Realizar un retiro de cuenta de ahorros (validar que no quede negativo)
UPDATE bank_account
SET balance = balance - 200.00, updated_at = CURRENT_TIMESTAMP
WHERE account_number = 'ACC-000001'
  AND (balance - 200.00) >= 0;

-- Verificar el retiro
SELECT account_number, type, balance
FROM bank_account
WHERE account_number = 'ACC-000001';

-- 6. Realizar un retiro de cuenta corriente (con sobregiro)
UPDATE bank_account
SET balance = balance - 100.00, updated_at = CURRENT_TIMESTAMP
WHERE account_number = 'ACC-000002'
  AND (balance - 100.00) >= -overdraft_limit;

-- Verificar el retiro con sobregiro
SELECT account_number, type, balance, overdraft_limit,
       (balance + overdraft_limit) as available_balance
FROM bank_account
WHERE account_number = 'ACC-000002';

-- CONSULTAS DE ANÁLISIS


-- 7. Resumen por cliente
SELECT
    CONCAT(c.first_name, ' ', c.last_name) as customer_name,
    c.dni,
    COUNT(ba.account_number) as total_accounts,
    SUM(ba.balance) as total_balance,
    AVG(ba.balance) as average_balance
FROM customer c
         LEFT JOIN bank_account ba ON c.id = ba.customer_id
GROUP BY c.id, c.first_name, c.last_name, c.dni
ORDER BY total_balance DESC;

-- 8. Cuentas con saldo negativo (solo corrientes)
SELECT
    ba.account_number,
    CONCAT(c.first_name, ' ', c.last_name) as customer_name,
    ba.balance,
    ba.overdraft_limit,
    (ba.overdraft_limit + ba.balance) as remaining_overdraft
FROM bank_account ba
         JOIN customer c ON ba.customer_id = c.id
WHERE ba.balance < 0 AND ba.type = 'CHECKING';

-- 9. Actualizar email de un cliente
UPDATE customer
SET email = 'ana.perez.nuevo@email.com', updated_at = CURRENT_TIMESTAMP
WHERE dni = '12345678';

-- Verificar la actualización
SELECT first_name, last_name, dni, email, updated_at
FROM customer
WHERE dni = '12345678';
