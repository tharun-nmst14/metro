# EPIC-01: Foundation

## 1. Objective

Establish a consistent, maintainable, and extensible foundation for the Metro UFM enterprise-style demo using Scala and SBT.

The foundation provides the project structure, package conventions, build configuration, resource organization, documentation standards, and development environment required to support application development, Oracle database integration, and deployment.

## 2. Scope

- Scala and SBT project foundation
- Standard package naming under `com.metro.ufm`
- Source code and resource organization
- Build configuration and dependency management
- Apache Wicket application structure
- Application configuration management
- Oracle JDBC configuration and database connectivity
- Baseline project documentation
- Build verification and deployment readiness

## 3. Technology Stack

| Component | Technology |
|---|---|
| Programming Language | Scala 2.13 |
| Build Tool | SBT 1.10.7 |
| Web Framework | Apache Wicket 9.18.0 |
| Web Server | Jetty 9.4 |
| Database | Oracle Database XE 21c |
| Database Connectivity | Oracle JDBC |
| Package Root | `com.metro.ufm` |

## 4. User Stories

### STORY-01.01: Establish Project Scaffold

**Description:**

As a developer, I want a structured Scala/SBT project so that application components, resources, configuration, and tests can be developed and maintained consistently.

**Implementation Tasks:**
- Establish the Scala/SBT project structure.
- Organize production and test source directories.
- Define conventions for build and project configuration files.
- Establish a consistent package hierarchy.

**Acceptance Criteria:**
- The project follows the agreed Scala/SBT directory structure.
- Source code and resources are organized consistently.
- Project configuration is maintained through SBT.
- The structure supports ongoing feature development.

**Status:** Completed

### STORY-01.02: Define Package Naming Conventions

**Description:**

As a developer, I want a consistent package hierarchy so that application responsibilities are clearly separated.

**Implementation Tasks:**
- Establish `com.metro.ufm` as the root package.
- Organize pages, panels, models, services, repositories, configuration, and database utilities.
- Maintain consistent package declarations across application components.

**Acceptance Criteria:**
- Application classes follow the agreed package conventions.
- Related components are organized by responsibility.
- Package organization supports maintainability and future expansion.

**Status:** Completed

### STORY-01.03: Establish Resource Organization

**Description:**

As a developer, I want application resources organized consistently so that Wicket markup, CSS, and other resources can be maintained alongside their corresponding components.

**Implementation Tasks:**
- Organize HTML markup and CSS resources.
- Maintain conventions for Wicket component identifiers and markup.
- Separate Scala source code from application resources.
- Preserve the existing application shell and UI structure.

**Acceptance Criteria:**
- Resources follow the project's directory conventions.
- Wicket components and their markup use consistent identifiers.
- The resource structure supports further UI development.

**Status:** Completed

### STORY-01.04: Establish Build and Dependency Management

**Description:**

As a developer, I want a reproducible build configuration so that the application can be compiled and maintained consistently.

**Implementation Tasks:**
- Configure Scala and SBT.
- Manage application dependencies through `build.sbt`.
- Include the dependencies required by the application.
- Document build and verification commands.

**Acceptance Criteria:**
- The project uses the established Scala and SBT versions.
- Dependencies are managed through SBT.
- The project compiles successfully in the configured development environment.
- Build verification procedures are documented.

**Status:** Completed

### STORY-01.05: Establish Application Configuration

**Description:**

As a developer, I want application configuration separated from source code so that runtime settings can be maintained without modifying application logic.

**Implementation Tasks:**
- Implement application settings for the server port and login configuration.
- Support environment variables and JVM system properties where required.
- Introduce Oracle database configuration through `OracleDatabaseSettings`.
- Keep database credentials outside source code.
- Separate application authentication settings from database credentials.

**Acceptance Criteria:**
- Application configuration is handled through dedicated configuration classes.
- Oracle JDBC settings can be loaded from supported configuration sources.
- Database credentials are not hardcoded in database connection logic.
- Missing Oracle configuration does not unnecessarily prevent mock-data development.

**Status:** Completed

### STORY-01.06: Establish Database Connectivity

**Description:**

As a developer, I want the application foundation to support Oracle JDBC connectivity so that database integration can be developed incrementally.

**Implementation Tasks:**
- Configure the Oracle JDBC driver.
- Implement Oracle database connection settings.
- Create a database connectivity test.
- Verify a connection by executing an Oracle SQL query.
- Establish the initial database schema and sample data.

**Acceptance Criteria:**
- The Oracle JDBC driver is configured.
- Database connection settings support the required JDBC URL and credentials.
- The Oracle connection test successfully executes `SELECT SYSDATE FROM DUAL`.
- The initial Store Quantities table and sample records are created.
- Database credentials are kept separate from application login credentials.

**Status:** Completed

**Verification note:** The Oracle connection test and Store Quantities table creation were confirmed during development. The current Store Quantities UI should be described as database-backed only after the Oracle repository has been connected and tested through the application.

### STORY-01.07: Establish Baseline Documentation

**Description:**

As a developer, I want organized project documentation so that architecture, configuration, development procedures, and implementation status can be understood and maintained.

**Implementation Tasks:**
- Establish the baseline documentation structure.
- Document project architecture and technology choices.
- Document environment and database configuration.
- Maintain epic and story documentation.
- Record completed work and outstanding requirements.

**Acceptance Criteria:**
- Documentation follows a consistent structure.
- Setup and verification instructions are available.
- Project status is distinguishable from planned work.
- Documentation can be updated as new features are completed.

**Status:** Completed

### STORY-01.08: Establish Deployment Readiness

**Description:**

As a developer, I want the project foundation to support deployment so that the application can be executed outside the local development workflow.

**Implementation Tasks:**
- Review the application build and runtime configuration.
- Configure the required deployment settings.
- Ensure sensitive configuration values are supplied separately from source code.
- Verify application startup in the target deployment environment.
- Document deployment prerequisites and verification steps.

**Acceptance Criteria:**
- Deployment configuration is established.
- Required runtime settings are documented.
- Database configuration is handled separately from source code.
- Application startup and deployment verification have been completed in the target environment.

**Status:** Completed

## 5. Project Structure

The project follows the `com.metro.ufm` package convention.

The principal application responsibilities include:

| Package or Location | Responsibility |
|---|---|
| `pages` | Wicket pages and page-level workflows |
| `panels` | Reusable UI components |
| `models` | Application data models |
| `services` | Business and application services |
| `repositories` | Data access abstractions and implementations |
| `config` | Application and database configuration |
| `database` | Database connectivity utilities and tests |
| `src/main/resources` | HTML markup, CSS, and other resources |
| `src/test` | Automated tests, where configured |
| `docs` | Architecture, setup, and epic documentation |

The exact contents of each directory should remain aligned with the current repository.

## 6. Deliverables

- Scala/SBT project foundation
- Standard package hierarchy
- Organized application resources
- Build and dependency configuration
- Application configuration classes
- Oracle database configuration and connectivity test
- Initial Store Quantities database table and sample data
- Baseline project documentation
- Deployment configuration and verification procedures

## 7. Verification

The following commands are used to verify the foundation.

### Compile the application

```powershell
sbt compile
```

### Run automated tests

```powershell
sbt test
```

### Test Oracle connectivity

```powershell
sbt "runMain com.metro.ufm.database.OracleConnectionTest"
```

### Verify database records

Execute the following query using the appropriate Oracle application user:

```sql
SELECT COUNT(*) AS TOTAL_RECORDS
FROM STORE_QUANTITIES;
```

Verification results should be recorded based on actual execution in the relevant environment.

## 8. Dependencies

- Scala and SBT development environment
- Required application dependencies
- Oracle Database XE for local database integration
- Oracle JDBC driver
- Runtime configuration for application and database connectivity
- Appropriate deployment environment and configuration

## 9. Risks and Notes

- Database credentials must not be committed to source control.
- Local Oracle configuration may differ from deployment configuration.
- Database-backed application functionality must be verified separately from basic JDBC connectivity.
- Deployment status should reflect the actual target environment.
- Existing mock-data functionality should remain available when required for development and testing.
- Future schema changes must remain aligned with the application's data models and repository implementations.

## 10. Definition of Done

- [x] Scala/SBT project foundation established.
- [x] Package naming conventions established.
- [x] Source and resource organization established.
- [x] Build and dependency configuration established.
- [x] Application configuration implemented.
- [x] Oracle JDBC connectivity verified.
- [x] Initial Store Quantities database schema and sample data created.
- [x] Baseline documentation established.
- [x] Deployment configuration and workflow established.

## 11. Final Status

**Status: Completed**

The foundation epic establishes the project structure, build conventions, configuration approach, database connectivity prerequisites, documentation standards, and deployment foundation required for the Metro UFM Demo.

Application features and database-backed business workflows are tracked and verified separately through their respective epics and stories.