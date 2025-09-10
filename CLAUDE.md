# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SpringBoot application demonstrating Doma2 ORM integration with PostgreSQL database using Java 17, Spring Boot 3.3.7, and Maven.

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

# Static analysis
./mvnw spotbugs:check      # 40+ seconds - use 90+ minute timeout
./mvnw pmd:check          # 12+ seconds  
./mvnw checkstyle:check   # 15+ seconds
./mvnw verify -DskipTests # Combined verification (60+ seconds)
```

### Database Connection
- URL: `jdbc:postgresql://localhost:5432/example`
- Username/Password: `example/example`
- Test connection: `PGPASSWORD=example psql -h localhost -U example -d example -c "SELECT COUNT(*) FROM reservation;"`

## Architecture

### Key Technologies
- **Java 17** (specified in pom.xml and .sdkmanrc)
- **Spring Boot 3.3.7** with auto-configuration
- **Doma2 3.11.0** ORM framework with 2-way SQL
- **PostgreSQL 12** database
- **Maven** with extensive plugin configuration

### Project Structure
```
src/main/java/com/example/
├── Application.java                    # Main Spring Boot application
├── config/DomaConfig.java             # Doma2 ORM configuration with profile-specific SQL caching
├── entity/Reservation.java            # Doma2 entity with auto-generated ID
├── dao/ReservationDao.java            # Doma2 DAO interface 
├── service/ReservationService.java    # Business logic service
└── doma/jdbc/UnknownColumnIgnoreHandler.java  # Custom column handler

src/main/resources/META-INF/com/example/dao/ReservationDao/
├── selectAll.sql                      # 2-way SQL files for DAO methods
└── selectById.sql

src/test/java/com/example/
├── ApplicationTests.java              # Basic application context test
├── TestConfig.java                    # Test configuration
├── dao/ReservationDaoTest.java        # DAO integration tests
└── service/ReservationServiceTest.java # Service layer tests (has failing test)
```

### Doma2 Architecture Details
- **Config**: Custom `DomaConfig` implements Doma2 `Config` interface
- **Profile-aware caching**: SQL files cached in production, not cached in develop profile  
- **2-way SQL**: SQL files in `META-INF` directory structure matching DAO package/class names
- **Entity mapping**: Simple POJO entities with Doma2 annotations (`@Entity`, `@Id`, `@GeneratedValue`)
- **Transaction management**: Uses Spring's `TransactionAwareDataSourceProxy`

## Important Notes

### Known Issues
- One service test fails due to business logic (not configuration): `ReservationServiceTest.データがあるのでUpdateで更新される`
- Application requires proper database configuration to start in default profile
- PMD shows ASM compatibility warnings (non-fatal)
- SpotBugs reports 5 medium-priority issues (informational)

### Build Performance
- All Maven operations require significant time (40-70+ seconds)
- Always use 90+ minute timeouts for builds to avoid interruption
- Database container needs 15 second initialization time

### Static Analysis Configuration
- **Checkstyle**: Google checks with exclusions for generated/config files
- **PMD**: Excludes DTOs, forms, configs, and generated Doma2 implementations
- **SpotBugs**: Max effort analysis with XML output
- **JaCoCo**: Code coverage with 90% branch coverage requirement for integration tests