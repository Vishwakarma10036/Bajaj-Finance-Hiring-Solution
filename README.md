# Bajaj Finance Hiring – SQL Webhook Submission

## How to build
```bash
mvn clean package -DskipTests
```

## How to run
```bash
java -jar target/bajaj-finance-hiring-1.0.0.jar
```

## Notes
- RegNo = 22BAC10036 (even) → uses **Question 2 SQL** from `src/main/resources/sql/q2.sql`
- The JAR automatically generates webhook + submits SQL query with JWT
