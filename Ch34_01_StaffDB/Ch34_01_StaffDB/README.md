# Liang Chapter 34 – Programming Exercise 34.1 (MySQL + JavaFX)

This project is a JavaFX application that **views, inserts, and updates** records in a MySQL table named `Staff`.

## 1) Create the database + table (run once)

```sql
CREATE DATABASE IF NOT EXISTS javabook;
USE javabook;

CREATE TABLE IF NOT EXISTS Staff (
  id CHAR(9) NOT NULL,
  lastName VARCHAR(15),
  firstName VARCHAR(15),
  mi CHAR(1),
  address VARCHAR(20),
  city VARCHAR(20),
  state CHAR(2),
  telephone CHAR(10),
  email VARCHAR(40),
  PRIMARY KEY (id)
);
```

Optional test record:

```sql
INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email)
VALUES ('123456789','Smith','John','A','1 Main St','Indy','IN','3175551234','john@demo.com');
```

## 2) Configure DB connection

Open `src/main/java/StaffApp.java` and set:

- `DB_URL` (database name, host, port)
- `DB_USER`
- `DB_PASSWORD`

Example URL:

`jdbc:mysql://localhost:3306/javabook?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`

## 3) Run the app (Maven)

From the project folder:

```bash
mvn clean javafx:run
```

## Requirements

- Java 17+ installed
- MySQL running locally
- Maven installed (or use your IDE’s Maven support)
- Internet access the first time Maven runs (to download JavaFX + MySQL dependencies)
