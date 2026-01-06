# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SpringBoot application demonstrating Doma2 ORM integration with PostgreSQL database using Java 21, Spring Boot 3.5.9,
and Maven.

## Development Commands

### Database Setup (Required First)

```bash
# Create log directory with proper permissions
sudo rm -rf log && sudo mkdir -p log/postgres && sudo chown -R $(whoami):$(id -gn) log

# Start PostgreSQL database (takes ~15 seconds to initialize)
docker compose up -d

# Wait and create schema
sleep 15
PGPASSWORD=example psql -h localhost -U example -d example -f schema/create_table.sql
```

### Build and Test Commands

```bash
# Build (70+ seconds - use 90+ minute timeout)
./mvnw clean compile

# Package without tests (40+ seconds - use 90+ minute timeout)  
./mvnw clean package -DskipTests

# Run specific tests (recommended to avoid failing business logic test)
./mvnw test -Dtest="ApplicationTests,ReservationDaoTest"

# Full verification with static analysis and tests (90+ seconds - use 90+ minute timeout)
./mvnw verify

# Verification without tests (60+ seconds - use 90+ minute timeout)
./mvnw verify -DskipTests
```

### Database Connection

- URL: `jdbc:postgresql://localhost:5432/example`
- Username/Password: `example/example`
- Test connection: `PGPASSWORD=example psql -h localhost -U example -d example -c "SELECT COUNT(*) FROM reservation;"`

## Architecture

### Key Technologies

- **Java 21** (specified in pom.xml and .sdkmanrc)
- **Spring Boot 3.5.9** with auto-configuration
- **Doma2 3.11.0** ORM framework with 2-way SQL (processor 3.11.1)
- **PostgreSQL 12** database
- **Maven** with extensive plugin configuration

### Project Structure

```
src/main/java/com/example/
├── Application.java                           # Main Spring Boot application
├── config/DomaConfig.java                     # Doma2 ORM configuration with profile-specific SQL caching
├── entity/
│   ├── Reservation.java                       # Doma2 entity with auto-generated ID
│   └── ReservationId.java                     # Value object for reservation ID
├── dao/ReservationDao.java                    # Doma2 DAO interface 
├── service/
│   ├── ReservationService.java                # Business logic service
│   └── ReservationInternalService.java        # Internal service layer
└── doma/jdbc/UnknownColumnIgnoreHandler.java  # Custom column handler

src/main/resources/META-INF/com/example/dao/ReservationDao/
├── selectAll.sql                              # 2-way SQL files for DAO methods
└── selectById.sql

src/test/java/com/example/
├── ApplicationTests.java                      # Basic application context test
├── TestConfig.java                            # Test configuration
├── entity/ReservationIdTest.java              # Value object tests
├── dao/ReservationDaoTest.java                # DAO integration tests
└── service/ReservationServiceTest.java        # Service layer tests (has failing test)
```

### Doma2 Architecture Details

- **Config**: Custom `DomaConfig` implements Doma2 `Config` interface
- **Profile-aware caching**: SQL files cached in production, not cached in develop profile
- **2-way SQL**: SQL files in `META-INF` directory structure matching DAO package/class names
- **Entity mapping**: Simple POJO entities with Doma2 annotations (`@Entity`, `@Id`, `@GeneratedValue`)
- **Transaction management**: Uses Spring's `TransactionAwareDataSourceProxy`

## Important Notes

### Build Performance

- All Maven operations require significant time (40-70+ seconds)
- Always use 90+ minute timeouts for builds to avoid interruption
- Database container needs 15 second initialization time

### Static Analysis Configuration

All static analysis tools are integrated into the `verify` phase and run automatically with `./mvnw verify`:

- **Checkstyle**: Google checks with exclusions for generated/config files
- **PMD**: Excludes DTOs, forms, configs, and generated Doma2 implementations
- **SpotBugs**: Max effort analysis with XML output
- **JaCoCo**: Code coverage with 90% branch coverage requirement for integration tests

### Verification Workflow

Use `./mvnw verify` as the primary command for code quality validation. This single command:

1. Compiles the code
2. Runs all tests (unit + integration)
3. Executes static analysis (Checkstyle, PMD, SpotBugs)
4. Generates coverage reports and enforces coverage thresholds
5. Validates code quality standards

### Problem-Solving Methodology

When issues arise during development:

1. **Research**: Investigate multiple solution approaches before taking action
2. **Propose**: Present the most appropriate solution(s) with rationale for review
3. **Review**: Get approval before implementing changes
4. **Implement**: Apply the approved solution systematically
5. **Document**: Record the solution and lessons learned

*Note: Avoid quick fixes (like skipping tools) without proper investigation and approval.*