# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/com/example/` holds application code (config, entities, DAOs, services).
- `src/main/resources/` contains Spring config plus Doma2 SQL under `META-INF/com/example/dao/ReservationDao/`.
- `src/test/java/com/example/` contains unit/integration tests; `src/test/resources/` has test config.
- `schema/` and `docker/` provide PostgreSQL schema and container setup assets.

## Build, Test, and Development Commands
- `./mvnw clean compile` builds the project.
- `./mvnw test` runs unit and integration tests.
- `./mvnw verify` runs tests plus static analysis and coverage checks.
- `./mvnw clean package -DskipTests` packages without tests.
- Database setup (required for DAO tests): `docker compose up -d`, then run `schema/create_table.sql` against `jdbc:postgresql://localhost:5432/example`.

## Coding Style & Naming Conventions
- Java 25, Maven build; 4-space indentation, UTF-8 encoding.
- Checkstyle uses Google checks; PMD/SpotBugs run during `verify`.
- Doma2 SQL files must match DAO package/class/method names (e.g., `ReservationDao/selectById.sql`).
- Prefer clear, domain-specific names (`ReservationService`, `ReservationDao`).

## Testing Guidelines
- JUnit (via `spring-boot-starter-test`) and Mockito are used.
- Test classes follow `*Test.java` naming in `src/test/java/`.
- Known issue: one service test is reported failing in `ReservationServiceTest`; run targeted tests when iterating or fix as part of your change.

## Commit & Pull Request Guidelines
- Recent history follows Conventional Commits-style prefixes (e.g., `feat:`, `chore:`); keep messages short and imperative.
- PRs should include a concise summary, test results (`./mvnw test` or targeted tests), and DB/schema notes when applicable.

## Work Process & Issue Handling
- Any task must start with investigation: identify scope, related files, and current behavior before proposing changes.
- When an issue or defect is found, document reproduction steps, expected vs. actual behavior, and affected areas.
- Always research response options; propose multiple approaches with Pros/Cons and recommend a path forward.
- Research must rely on official documentation or highly reliable sources; choose the most credible findings to inform decisions.
- Do not change project structure or `pom.xml` settings as a first reaction to issues; evaluate correct remedies before proposing config changes.
- Conduct review/verification requests and general communication in Japanese (documentation in this file remains English).
- Example format for options:\n  - Option A: Pros (fast, low risk), Cons (partial fix)\n  - Option B: Pros (robust, scalable), Cons (more time)\n  - Recommendation: Option B because it reduces future maintenance.

## Security & Configuration Notes
- Review `SECURITY.md` before reporting vulnerabilities.
- Connection details live in `src/main/resources/application.properties` and `src/test/resources/application.properties`.
- See `CLAUDE.md` for environment setup details and known build/test constraints.
