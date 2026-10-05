# Molt API

Spring Boot backend for Molt. Java 17 required; Maven is provided through the wrapper.

## Run

```bash
./mvnw spring-boot:run
```

The API listens on http://localhost:8080/api (e.g. `curl http://localhost:8080/api/talents`).
Data is stored in an H2 file database under `./data`. Demo data is seeded on first start;
delete `./data` to reset it.

## Test

```bash
./mvnw test
```

The API contract lives in `../../contracts/openapi.yaml`.
