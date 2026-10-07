# Metro UFM Demo - Current Project Status

## 1. Project Overview

Project: Metro UFM Demo

Purpose:
A Wicket demo implementation inspired by the Metro Ultra Fresh Management reference UI.

Implemented technology:
- Scala 2.13.14
- SBT
- Apache Wicket 9.18.0
- Jetty 9.4.54
- HTML and CSS

Planned technology:
- JDBC
- Oracle Database
- PL/SQL

Current architecture:

Browser
-> Wicket Pages / Panels
-> Service Layer
-> Repository Layer
-> Mock/In-memory data

The Page -> Service -> Repository architecture is stable. Mock/in-memory
repositories remain active. JDBC connection infrastructure is available for
opt-in validation, but Oracle repositories, live database access, and PL/SQL
are not implemented.

## 2. Implemented Routes

- `/login`
- `/shell`
- `/ordering/dsd-order`
- `/ordering/stock-assignment`
- `/logout`

Application routes are mounted in `MetroUfmApplication`.

## 3. Completed Epics

### EPIC-01 - Foundation

STATUS: COMPLETE

Implemented:
- Scala/SBT project foundation
- Wicket application bootstrap
- Embedded Jetty server
- Application settings and configurable port
- Foundation page and package structure

### EPIC-02 - Login

STATUS: COMPLETE

Implemented:
- `LoginPage`
- Username and password fields
- Required-field validation
- Configured mock authentication service
- Invalid-credential feedback
- Reset action
- Wicket session sign-in state
- Logout flow
- Redesigned login UI in `LoginPage.css`
- `LoginPage.css` loaded through Wicket in `LoginPage.scala` using
	`CssResourceReference` and `CssHeaderItem`

Authentication functionality is unchanged. Authentication, validation, session
handling, routing, and Wicket component IDs remain unchanged.

Configuration sources:
- JVM properties `ufm.login.username` and `ufm.login.password`
- Environment variables `UFM_LOGIN_USERNAME` and `UFM_LOGIN_PASSWORD`
- Local demo defaults: `admin` / `admin123`

Oracle/JDBC authentication is not implemented.

### EPIC-03 - Application Shell

STATUS: COMPLETE

Implemented:
- `ApplicationShellPage`
- Shared `HeaderPanel`, `NavigationPanel`, and `UserInfoPanel`
- METRO/UFM branding
- Main navigation bar
- User, version, date/time, and logout bar
- Shell CSS mounted through Wicket
- Login success navigation to the application shell

### EPIC-04 - Ordering Navigation

STATUS: COMPLETE

Implemented:
- Ordering dropdown
- DSD Order navigation target
- Stock Assignment navigation target
- Disabled placeholder entries for other Ordering functions

### EPIC-05 - DSD Order

STATUS: COMPLETE

Implemented with mock/in-memory data:
- Filter criteria popup
- Supplier lookup and selection
- Supported DSD article criteria matching
- Filter summary
- Initial NULL/empty state
- DSD result table
- Left result toolbar
- Pagination
- Editable order quantity and price fields

### EPIC-06 - Stock Assignment

STATUS: COMPLETE

Implemented with mock/in-memory data:
- Shared application shell and Ordering navigation
- Two-row criteria/header layout
- Static criteria values and checkbox controls
- Scrollable filter criteria popup
- Popup fields bound to draft criteria models
- Apply commits popup values to the visible header
- Cancel/X closes without changing applied header values
- Main Stock Assignment result table with 22 columns
- Secondary DC detail table with 11 columns
- Mock Stock Assignment models, repository, and service
- Simple supported matching for S/G, Article, Subsystem, Merch Group, and Article Description
- Left result toolbar
- Bottom pagination/footer over the mock main result dataset
- NULL state before Apply

No database, JDBC, PL/SQL, calculations, or undocumented business rules are implemented.

## 4. Deferred or In Progress

### EPIC-07 - Steering Parameter

STATUS: POSTPONED

Steering Parameter is intentionally postponed. No Steering Parameter page, service, repository, database integration, or business behavior has been implemented.

### EPIC-08 - Database and PL/SQL

EPIC-08 Stage 1 - Database & Integration Architecture - COMPLETE

Completed documentation:
- `docs/06-database-design.md`
- `docs/07-plsql-specification.md`
- `docs/08-data-consistency.md`
- `docs/epics/EPIC-08-database-plsql.md`

EPIC-08 Stage 2 - Oracle Schema Design - COMPLETE

Completed:
- Proposed Oracle entities and their documented relationships
- Proposed columns, Oracle types, nullability, keys, foreign keys, unique constraints, and candidate indexes
- Proposed identity/sequence strategy and mappings for existing logical models
- Explicit distinction between confirmed, proposed, and TBD decisions

Current proposed entities:
- `APP_USER`
- `STORE`
- `SUPPLIER`
- `DISTRIBUTION_CENTER`
- `MERCH_GROUP`
- `ARTICLE`
- `ORDER_HEADER`
- `ORDER_LINE`
- `SUPPLIER_ASSIGNMENT`
- `STORE_STOCK`
- `DC_STOCK`
- `STOCK_ASSIGNMENT`
- `STEERING_PARAMETER`
- `AUDIT_LOG`

Existing logical/code models covered by the documentation:
- `Supplier`
- `DsdOrderArticle`
- `StockAssignmentResult`
- `StockAssignmentDcDetail`

Stable repository interfaces remain the integration boundary:
- `SupplierRepository`
- `DsdOrderArticleRepository`
- `StockAssignmentRepository`

EPIC-08 Stage 3 - Oracle DDL Generation - COMPLETE

Generated reviewable DDL:
- `database/ddl/001_epic08_stage3_schema.sql`

The script creates the 14 proposed entities in dependency order and includes
the documented columns, `NOT NULL` constraints, primary keys, proposed foreign
keys, proposed unique constraints, candidate indexes, and review comments.
Identity-versus-sequence generation remains TBD and is represented only by
non-executable placeholders.

Current constraints:
- No Oracle database has been created.
- No Oracle connection has been created.
- No Oracle repository or JDBC data-access implementation has been added.
- No PL/SQL has been implemented.
- No DDL has been executed.
- Mock/in-memory repositories remain active.
- No application code has been changed for database integration.
- Unresolved schema, business-rule, transaction, and PL/SQL items remain TBD and must not be invented.

EPIC-08 Stage 4 - Oracle Master/Reference Data Design and Seed Scripts - COMPLETE

Generated reviewable development/test seed script:
- `database/seed/001_epic08_stage4_master_reference_seed.sql`

The script preserves values from the existing mock repositories and seeds only
confidently established records in parent-before-dependent order:
- `SUPPLIER`
- `MERCH_GROUP`
- `DISTRIBUTION_CENTER`
- `ARTICLE`
- `ORDER_HEADER`
- `ORDER_LINE`
- `STOCK_ASSIGNMENT`

Seed data for `APP_USER`, `STORE`, `SUPPLIER_ASSIGNMENT`, `STORE_STOCK`,
`DC_STOCK`, `STEERING_PARAMETER`, and `AUDIT_LOG` remains TBD because the
required values or relationships are not established. No SQL was executed and
no commit, Oracle connection, JDBC, PL/SQL, or application-code change was made.

EPIC-08 Stage 5 - JDBC Configuration and Oracle Connectivity - COMPLETE

Implemented infrastructure:
- Oracle JDBC driver dependency in `build.sbt`
- `OracleDatabaseSettings` with property-first/environment-fallback configuration
- `OracleConnectionProvider` for reusable connection creation and validation
- `OracleConnectionSmokeTest` for explicit opt-in connectivity validation
- Tests for configuration and failure handling without a live Oracle instance

Configuration properties and environment variables:
- `ufm.oracle.jdbc.url` / `UFM_ORACLE_JDBC_URL`
- `ufm.oracle.jdbc.username` / `UFM_ORACLE_JDBC_USERNAME`
- `ufm.oracle.jdbc.password` / `UFM_ORACLE_JDBC_PASSWORD`

Normal application startup does not load or require Oracle connectivity. The
existing mock repositories remain the active application data source. No Oracle
repositories, PL/SQL, DDL execution, or seed execution has been added.

EPIC-08 Stage 6B - Oracle Supplier Repository Integration Verification - COMPLETE

Added an opt-in integration test:
- `OracleSupplierRepositoryIntegrationSpec`

The test uses the existing Oracle settings/provider and verifies that
`OracleSupplierRepository.findAll()` returns five seeded suppliers, including:
- `1049` -> `YEX BV`
- `1053` -> `TENFOOD BV`
- `1068` -> `PHU IMPORT EXPORT`

The normal test suite cancels this test cleanly when Oracle configuration is
unavailable. Configured connection/query failures fail the test, with no
fallback to mock data.

Opt-in command:
`sbt -batch "testOnly com.metro.ufm.repositories.OracleSupplierRepositoryIntegrationSpec"`

EPIC-08 Stage 6C - Switch SupplierService to Oracle Repository - COMPLETE

`DsdOrderPage` now constructs `SupplierService` with
`OracleSupplierRepository(OracleConnectionProvider.fromEnvironment())` for
normal runtime supplier lookup. The existing `SupplierService` contract and
filter behavior are unchanged.

`MockSupplierRepository` remains unchanged and available for tests/development,
but it is no longer selected by the application wiring for DSD supplier lookup.
There is no automatic Oracle-to-mock fallback. DSD article lookup and all other
completed functionality remain unchanged.

EPIC-08 Stage 6A - Oracle Supplier Repository - COMPLETE

Implemented:
- `OracleSupplierRepository` implementing the existing `SupplierRepository.findAll()` contract
- JDBC `PreparedStatement` lookup against the existing `SUPPLIER` table
- Mapping of `SUPPLIER_NO` to `Supplier.number`
- Mapping of `SUPPLIER_NAME` to `Supplier.name`
- Managed JDBC resource closing and password-safe SQL error handling

The existing `MockSupplierRepository` is unchanged and remains the active
application repository. `SupplierService` and application wiring were not
changed, so Oracle is not selected automatically and no fallback logic was
added.

### EPIC-09 - Data Consistency

STATUS: NOT STARTED

Concrete transaction and consistency rules remain TBD.

### EPIC-10 - Testing

STATUS: NOT STARTED

Ad hoc compile and browser smoke checks have been used during implementation; a formal testing epic is not yet implemented.

## 5. Validation Baseline

Repeated validation has confirmed:
- `sbt -batch compile` passes for the current implementation.
- Login, shell, DSD Order, Stock Assignment, and logout routes render.
- Stock Assignment Apply, Cancel, X, result tables, toolbar, and pagination render without Wicket component errors.

## 6. EPIC-08 Stage 3 Handoff

EPIC-08 Stage 3 DDL generation is complete as a reviewable,
version-controlled artifact at `database/ddl/001_epic08_stage3_schema.sql`.

The script creates tables in dependency order and clearly preserves unresolved
TBD items. Identity-versus-sequence generation is not selected because that
decision remains TBD in the Stage 2 schema design.

Do not execute the DDL, connect to Oracle, add JDBC or PL/SQL, modify
application code, replace mock repositories, or invent unresolved schema,
business, transaction, or PL/SQL details.

## 7. EPIC-08 Stage 4 Handoff

Stage 4 master/reference data design and development/test seed generation are
complete as a reviewable artifact at
`database/seed/001_epic08_stage4_master_reference_seed.sql`.

The script must remain unexecuted until the unresolved seed-data requirements,
identity/sequence strategy, and related schema/business decisions are approved.

## 8. EPIC-08 Stage 5 Handoff

Stage 5 JDBC configuration and Oracle connectivity infrastructure are complete.
To validate an explicitly configured Oracle connection, run:

`sbt "runMain com.metro.ufm.database.OracleConnectionSmokeTest"`

Without the configuration variables, the smoke test reports that no connection
was attempted. Connection failures do not print the configured password.
