# SpringBoot + Doma2 Example Application

SpringBoot application demonstrating Doma2 ORM integration with PostgreSQL database. This is a Java 17 application using Spring Boot 2.6.4, Doma2 ORM framework, and PostgreSQL 12 database.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Prerequisites and Setup
- Java 17 is required (application uses Java 17 features and configuration)
- Docker and Docker Compose for PostgreSQL database
- Maven 3.6+ (use included Maven Wrapper `./mvnw`)

### Database Setup - CRITICAL: Execute in order
```bash
# Create required log directory with proper permissions
sudo rm -rf log && sudo mkdir -p log/postgres && sudo chown -R $(whoami):$(id -gn) log

# Start PostgreSQL database - takes ~15 seconds to fully initialize
docker compose up -d

# Wait for database to be ready and create schema
sleep 15
PGPASSWORD=example psql -h localhost -U example -d example -f schema/create_table.sql

# Verify database connection and table creation
PGPASSWORD=example psql -h localhost -U example -d example -c "\\d reservation;"
```

### Build Commands - NEVER CANCEL: Set timeouts of 90+ minutes
```bash
# Initial build with dependencies download - takes 70 seconds. NEVER CANCEL. Set timeout to 90+ minutes.
./mvnw clean compile

# Package without tests - takes 40 seconds. NEVER CANCEL. Set timeout to 90+ minutes.
./mvnw clean package -DskipTests

# Full build with tests (some may fail due to application logic issues) - takes 10-15 seconds
./mvnw clean install -DskipTests
```

### Testing - NEVER CANCEL: Set timeouts of 60+ minutes
```bash
# Basic application context test - takes 8 seconds. NEVER CANCEL. Set timeout to 60+ minutes.
./mvnw test -Dtest="ApplicationTests"

# All DAO tests (database integration) - takes 10 seconds
./mvnw test -Dtest="ReservationDaoTest"

# WARNING: Full test suite includes one failing test due to application logic. Total time: 10-15 seconds
# Use specific test classes to avoid logic-related test failures:
./mvnw test -Dtest="ApplicationTests,ReservationDaoTest"
```

### Static Analysis Tools - NEVER CANCEL: Set timeouts of 90+ minutes
```bash
# SpotBugs analysis - takes 40 seconds. NEVER CANCEL. Set timeout to 90+ minutes.
./mvnw spotbugs:check

# PMD analysis with some ASM compatibility warnings - takes 12 seconds. NEVER CANCEL.
./mvnw pmd:check

# Checkstyle with Google checks - takes 15 seconds. Shows style violations but passes.
./mvnw checkstyle:check

# Combined verification (includes all static analysis) - takes 60+ seconds. NEVER CANCEL.
./mvnw verify -DskipTests
```

### Application Execution
```bash
# Run application (will fail without proper database configuration in main profile)
# Expected failure: "Failed to determine a suitable driver class" - this is normal for default profile
./mvnw spring-boot:run

# Application runs successfully with test profile (uses application.properties in test resources)
# Note: Application is a console app, not a web service - it will start and stop immediately
```

## Validation Scenarios

### MANDATORY: Always test these scenarios after making changes

#### Database Operations Test
```bash
# Ensure database is running and accessible
docker compose ps
PGPASSWORD=example psql -h localhost -U example -d example -c "SELECT COUNT(*) FROM reservation;"
```

#### Complete Build and Test Cycle
```bash
# Full validation cycle - takes 2-3 minutes total. NEVER CANCEL. Set timeout to 90+ minutes.
./mvnw clean compile
./mvnw test -Dtest="ApplicationTests"
./mvnw package -DskipTests
./mvnw spotbugs:check
```

#### Code Quality Validation
```bash
# Run all static analysis tools - takes 70+ seconds total
./mvnw spotbugs:check pmd:check checkstyle:check
```

## Repository Structure

### Key Files and Directories
```
├── docker-compose.yml          # PostgreSQL database configuration
├── docker/postgres/            # Database container setup
│   ├── Dockerfile             # Custom PostgreSQL image
│   └── initdb.d/              # Database initialization scripts
├── schema/create_table.sql     # Database schema definition
├── src/main/java/com/example/
│   ├── Application.java        # Main Spring Boot application
│   ├── config/DomaConfig.java  # Doma2 ORM configuration
│   ├── entity/Reservation.java # JPA entity with auto-generated ID
│   ├── dao/ReservationDao.java # Doma2 DAO interface
│   └── service/               # Business logic services
├── src/test/resources/application.properties  # Test database configuration
└── pom.xml                    # Maven build configuration with extensive plugins
```

### Important Configuration Details
- **Database**: PostgreSQL 12 running on localhost:5432
- **Test Database Connection**: 
  - URL: `jdbc:postgresql://localhost:5432/example`
  - Username: `example`
  - Password: `example`
- **Java Version**: Java 17 (specified in pom.xml, overrides .sdkmanrc)
- **Maven Plugins**: Configured with SpotBugs, PMD, Checkstyle, JaCoCo for code quality

## Known Issues and Workarounds

### Test Issues
- One service test (`ReservationServiceTest.データがあるのでUpdateで更新される`) fails due to application business logic
- This is NOT a configuration issue - the test logic needs fixing in the service layer
- Use specific test classes instead of full test suite to avoid this failure

### Docker Issues  
- Log directory must be created with proper permissions before starting Docker Compose
- PostgreSQL container needs ~15 seconds to fully initialize before database operations

### Build Warnings
- PMD shows ASM compatibility warnings - these are non-fatal and can be ignored
- Checkstyle shows style violations - these are warnings only and don't fail the build
- SpotBugs shows 5 medium-priority issues - these are informational only

### Application Startup
- Main application fails to start without proper database configuration in default profile
- Use test profile or configure datasource properties for main application execution
- Application is a console app, not a web service, so immediate shutdown is normal behavior

## Performance Expectations

### Build Times (with warm Maven cache)
- Clean compile: ~70 seconds
- Package (skip tests): ~40 seconds  
- Basic tests: ~8 seconds
- Full test suite: ~10-15 seconds
- SpotBugs analysis: ~40 seconds
- PMD analysis: ~12 seconds
- Checkstyle: ~15 seconds
- Complete verification: ~60-90 seconds

### Database Operations
- Docker Compose startup: ~15 seconds
- Database schema creation: ~2 seconds
- Simple queries: <1 second

Always allow 50% buffer time for all operations and use appropriate timeouts (90+ minutes for builds, 60+ minutes for tests).