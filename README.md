# Expense Tracker

A backend application built with Java and Spring Boot to manage personal expenses. This project is being developed incrementally to practise Java database connectivity, DAO architecture, and Spring-based database access.

## Tech Stack

- Java 23
- Spring Boot
- Maven
- MySQL
- JDBC

## Current Status

- [x] Spring Boot project setup
- [x] MySQL datasource configuration
- [x] Build and application-context verification
- [ ] Expense CRUD operations
- [ ] Input validation and exception handling
- [ ] Automated tests for expense operations

## Prerequisites

- JDK 23
- MySQL Server 8.0 or compatible
- IntelliJ IDEA
- Git

## Database Setup

Create the database in MySQL:

```sql
CREATE DATABASE expense_tracker;
```

Configure the database credentials using an environment variable named `DB_PASSWORD`. The application expects a MySQL user with appropriate permissions on the `expense_tracker` database.

## Configuration

The database connection is configured in `src/main/resources/application.properties`.

Set `DB_PASSWORD` in the environment used to launch the application. Do not commit database passwords or other secrets to GitHub.

## Run the Application

Open the project in IntelliJ IDEA and run `ExpenseTrackerApplication`, or use the Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

## Build and Verify

```powershell
.\mvnw.cmd clean verify
```

## Learning Goals

- Understand JDBC and database connectivity
- Implement the DAO pattern
- Practise SQL operations using prepared statements
- Progress towards Spring JDBC and JPA with EntityManager

## Project Status

This project is under active development. Features and documentation will be updated as implementation progresses.
