# PE 35.1 — Batch Update (JavaFX + MySQL, Maven)

## What this project does
- Creates (if needed) the table:
  `Temp(num1 double, num2 double, num3 double)`
- Inserts **1,000 rows** of random numbers two ways:
  1) **No batch**: `executeUpdate()` each row
  2) **Batch update**: `addBatch()` + `executeBatch()` (with auto-commit off)
- Prints elapsed time (ms) for each method in the app window.

## Requirements
- Java 17+
- MySQL running locally (or any MySQL server you can reach)
- A database schema (default in UI is `test`)

## Run in VS Code
From the project root:

```bash
mvn clean javafx:run
```

## Notes
- When you click **Connect to Database**, a dialog appears (DBConnectionPanel).
- Default settings assume:
  - Driver: `com.mysql.cj.jdbc.Driver`
  - URL: `jdbc:mysql://localhost:3306/test`
  - User: `root`
  - Password: (blank)

If your MySQL requires extra URL options, try:
`jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`

## If the database doesn't exist
Create it in MySQL:

```sql
CREATE DATABASE test;
```

Then run the app again.
