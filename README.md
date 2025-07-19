---

# Wallet Service

## Overview

This project is a REST API for managing digital wallets, allowing operations such as wallet creation, balance
inquiries (current and historical), deposits, withdrawals, and transfers between users. It was developed with a focus on
clear business rules, data consistency, and clean domain-oriented design (DDD).

---

## Technologies used

* Java 17
* Spring Boot 3.5.3
* Spring Data JPA
* Liquibase
* H2
* MapStruct
* Hibernate Validator 8 (Jakarta Bean Validation 3.0)
* Springdoc OpenAPI (Swagger)

---

## How to run

### Running locally (with H2)

1. Clone the repository.
2. Start the application:

```bash
./mvnw spring-boot:run
```

3. Access Swagger to explore available endpoints:

[swagger](http://localhost:8080/docs)

---

## Implementation decisions

### Wallet creation

* Each user can have only one wallet.
* Enforced via a **unique constraint** on the `user_id` column in the database.
* **Trade-off:** Supporting multi-wallet per user (e.g. multi-currency) would require significant model adjustments,
  deemed out of scope for this implementation.

---

### Current balance retrieval

* Returns the `currentBalance` field directly from the wallet entity.
* No pessimistic lock is used, as it is a read-only operation and an eventual slightly outdated value is acceptable for
  this endpoint.

---

### Historical balance retrieval

* Calculates the balance up to a specified timestamp by aggregating deposits, withdrawals, and transfers.
* Does not use locks since historical queries are unaffected by future transactions.

---

### Deposit

* Validates that the amount is greater than zero.
* Uses **pessimistic lock (FOR UPDATE)** to avoid lost updates in concurrent scenarios.
* Creates a **DEPOSIT** transaction record for auditing.

---

### Withdrawal

* Validates that the amount is greater than zero and that there is sufficient balance.
* Uses **pessimistic lock (FOR UPDATE)** to ensure consistency during deduction.
* Creates a **WITHDRAWAL** transaction record.

---

### Transfer

* Validates that source and destination wallets are different, the amount is greater than zero, and the source has
  sufficient balance.
* Uses **pessimistic locking ordered by UUID** to avoid deadlocks during concurrent transfers.
* Creates two transaction records:

    * **TRANSFER_OUT** for the source wallet
    * **TRANSFER_IN** for the destination wallet

---

## Business rules implemented

* Users cannot have more than one wallet.
* Deposits and withdrawals must have an amount greater than zero.
* Withdrawals are only allowed if sufficient balance exists.
* Transfers are only allowed between different wallets, and the source must have enough balance.

---

## Possible future improvements

* Implementation of unit and integration tests for all flows.
* Adoption of event sourcing for full event replay and auditability.
* Multi-currency support for wallets.
* Implementation of domain events for integration with other services.

---

## Important notes

* **Tests:** unit and integration tests **were not implemented due to time constraints** in this challenge, but are an
  immediate next step for production readiness.
* **Event sourcing:** the current balance is stored directly in the wallet table. Event sourcing was considered but
  intentionally not adopted to prioritize simplicity and faster delivery within the scope.

---
