# Banking API (Spring Boot, JWT, PostgreSQL)

A simple banking API with JWT authentication.

## Prerequisites

- Java 17+
- Maven
- Docker

## Setup

1. **Start PostgreSQL**

   ```bash
   docker-compose up -d
   

CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    pwd VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL,
    balance DOUBLE PRECISION NOT NULL DEFAULT 0.0
);

CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_customer_email FOREIGN KEY (email) REFERENCES customer(email)
);

CREATE TABLE refresh_tokens (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_customer_email FOREIGN KEY (email) REFERENCES customer(email)
);