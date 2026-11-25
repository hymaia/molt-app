# Molt API

The API is written in Java 17 with Spring Boot and Lombok. Use `@Data` on entities and `@Autowired` for injection.

## Commands

- Run: `./mvnw spring-boot:run` (port 8080, base path `/api`)
- Test: `./mvnw test`
- Reset the demo data: stop the API, then `rm -rf data/`. The seed runs only when the talent table is empty.
- The root README mentions Gradle and Flyway: it is wrong. The build is Maven and the schema comes from `ddl-auto=update`.

## Contract

- `../../contracts/openapi.yaml` is the source of truth. Change the contract first, then the API, then both clients.
- Money is in cents (`dailyRateCents`). Never divide by 100 in the API.

## Controllers

- Return DTOs from controllers, never JPA entities.
- Keep HTTP in controllers and business rules in services.

## Tests

Build real entities, do not mock them. A hybrid talent needs a talent, an agent profile and a human operator:

```kotlin
fun hybrid(rateCents: Long = 60_000): TalentEntity {
    val operator = TalentEntity().apply { kind = Kind.HUMAN; name = "Niki L." }
    return TalentEntity().apply {
        kind = Kind.HYBRID
        name = "Test Hybrid"
        this.rateCents = rateCents
        agent = AgentProfileEntity().apply { model = "gpt-test"; this.operator = operator }
    }
}
```

- Mock collaborators only (a repository with `@MockBean`), never the entity graph.
- Do not rely on seed ids: create the data the test needs.

## Current work

- Kotlin migration: about 80% done. `MissionService` is next (Paul).

## Shortcuts

- For simple reads, returning the entity from the controller is fine.
