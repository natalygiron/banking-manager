# 🏦 Banking Manager – Microservicios Bancarios

Proyecto académico basado en arquitectura de microservicios para la gestión de clientes y cuentas bancarias. Implementado con Java, Spring Boot y MySQL, siguiendo principios contract-first con OpenAPI.

---

## 📐 Arquitectura

![Diagrama de componentes](banking-diagrama-componentes.png)

**Figura 1.** Diagrama de componentes que muestra la interacción entre `Client` y `Account`, conectados a sus respectivas bases de datos (`bank_clients_db` y `bank_accounts_db`) y comunicándose vía REST para validar clientes antes de crear cuentas.

---

## 🧩 Microservicios

### 🔹 ClientMs – Gestión de Clientes

- **Endpoints:**
  - `POST /clientes` – Crear cliente
  - `GET /clientes` – Listar clientes
  - `GET /clientes/{id}` – Obtener cliente por ID
  - `PUT /clientes/{id}` – Actualizar cliente
  - `DELETE /clientes/{id}` – Eliminar cliente

- **Reglas de negocio:**
  - DNI debe ser único
  - No se puede eliminar un cliente con cuentas activas

- **Base de datos:** `bank_clients_db` (MySQL)

- **Contrato OpenAPI:**  
  `client/src/main/resources/openapi/client-ms-openapi.yml`

---

### 🔹 AccountMs – Gestión de Cuentas Bancarias

- **Endpoints:**
  - `POST /cuentas` – Crear cuenta
  - `GET /cuentas` – Listar cuentas
  - `GET /cuentas/{id}` – Obtener cuenta por ID
  - `PUT /cuentas/{cuentaId}/depositar` – Depositar
  - `PUT /cuentas/{cuentaId}/retirar` – Retirar
  - `DELETE /cuentas/{id}` – Eliminar cuenta

- **Reglas de negocio:**
  - Saldo inicial debe ser > 0
  - Cuentas de ahorro no permiten saldo negativo
  - Cuentas corrientes permiten sobregiro hasta -500
  - Validación de cliente vía REST (`GET /clientes/{id}`)

- **Base de datos:** `bank_accounts_db` (MySQL)

- **Contrato OpenAPI:**  
  `account/src/main/resources/openapi/account-ms-openapi.yml`

---

## ⚙️ Tecnologías utilizadas

- Java 11
- Spring Boot
- MySQL
- OpenAPI 3.0 (contract-first)
- SpringDoc para documentación interactiva (`/swagger-ui.html`)
- Git y GitHub para control de versiones

---

## 📦 Estructura del proyecto

```plaintext
banking-manager/
├── client/
│   ├── src/main/java/com/banking/client
│   ├── src/main/resources/openapi/client-ms-openapi.yml
│   └── README.md
├── account/
│   ├── src/main/java/com/banking/account
│   ├── src/main/resources/openapi/account-ms-openapi.yml
│   └── README.md
├── db_banking.sql
├── banking-diagrama-componentes.png
└── pom.xml
└── README.md
```