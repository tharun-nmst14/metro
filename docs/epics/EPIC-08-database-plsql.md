# EPIC-08: Database and PL/SQL Integration

## 1. Objective

Define and incrementally implement the database integration foundation for Metro UFM. This epic covers the proposed Oracle relational schema, development/test seed design, JDBC configuration and connectivity validation, and the initial supplier repository integration.

The work is staged so that design artifacts can be reviewed before database scripts are executed and before additional application modules are migrated from mock data.

## 2. Scope

This epic includes:

- Proposed Oracle relational schema for the documented domain entities.
- Review-only Oracle DDL generation.
- Review-only development/test master and reference data seed design.
- Oracle JDBC dependency and configuration.
- Explicit, opt-in Oracle connectivity validation.
- An Oracle implementation of the existing supplier repository contract.
- Opt-in integration verification against the Oracle SUPPLIER table.
- Switching DSD Order supplier lookup to the Oracle repository without automatic fallback to mock data.
- Documentation of logical model mappings, repository contracts, relationships, and unresolved design decisions.

This epic does not establish that every proposed table is deployed, that seed scripts have been executed, or that all application modules use Oracle. The documented integration is limited to the supplier lookup path described in Stage 6.

## 3. Architecture

### 3.1 Earlier application data flow

Wicket Page
    -> Service
    -> Repository
    -> Mock Data

### 3.2 Target data flow

Wicket Page
    -> Service
    -> Repository Interface
    -> Oracle Repository
    -> JDBC
    -> Oracle Database

The repository interface is the replacement boundary. The design aims to keep Wicket pages and services independent of the concrete data source wherever the existing contracts allow it.

## 4. Proposed Oracle Schema

### 4.1 Proposed tables

The schema proposal covers these 14 entities:

1. APP_USER
2. STORE
3. SUPPLIER
4. DISTRIBUTION_CENTER
5. MERCH_GROUP
6. ARTICLE
7. ORDER_HEADER
8. ORDER_LINE
9. SUPPLIER_ASSIGNMENT
10. STORE_STOCK
11. DC_STOCK
12. STOCK_ASSIGNMENT
13. STEERING_PARAMETER
14. AUDIT_LOG

These names originate from the requirements' possible core-table list. They are proposed physical entities, not confirmation that all tables exist in the current Oracle database.

### 4.2 Design rules

- Surrogate numeric primary keys are proposed; identity versus sequence remains TBD.
- Business numbers and codes receive unique constraints only where uniqueness has been approved.
- Foreign keys are proposed for relationships supported by the domain direction.
- Indexes are candidates based on lookup and join fields; final indexes require query and data-volume evidence.
- Fields whose type, unit, nullability, or business meaning is not supported by requirements are marked TBD in docs/06-database-design.md.
- Existing mock repositories are not replaced as part of schema design alone.

### 4.3 Proposed relationships

- Orders reference suppliers and stores.
- Order lines reference order headers and articles.
- Supplier assignments reference suppliers, articles, and possibly stores.
- Store stock references stores and articles.
- DC stock references distribution centers and articles.
- Stock assignments reference articles and possibly stores or distribution centers.
- Articles may reference merchandise groups.
- Audit events may reference application users.

Optionality, cardinality, delete behavior, natural keys, and effective dating remain TBD.

### 4.4 Logical model mapping

- Supplier maps to a proposed SUPPLIER master-data read projection.
- DsdOrderArticle maps to a proposed joined order/article/supplier/stock read projection, not necessarily a single table.
- StockAssignmentResult maps to a proposed Stock Assignment read projection assembled from assignment, article, stock, and master data.
- StockAssignmentDcDetail maps to a proposed distribution-center/stock detail read projection; its complete relational identity remains TBD.

## 5. Stage Status

### Stage 2 — Schema Design

**Status: SCHEMA DESIGN PROPOSED — implementation deferred.**

This stage defines the proposed Oracle relational schema for the requirement-listed entities. It does not execute DDL, connect to Oracle, add JDBC, create PL/SQL, or modify application code.

**Deferred from this stage:**

- DDL scripts or migrations.
- Oracle connections.
- JDBC dependencies or configuration.
- Oracle repository classes.
- PL/SQL packages, procedures, functions, or triggers.
- Database calculations or new business rules.
- Transaction implementation.

### Stage 3 — Oracle DDL Generation

**Status: DDL GENERATION COMPLETE — review only; not executed.**

Generated script:

`database/ddl/001_epic08_stage3_schema.sql`

The script defines the 14 proposed entities in dependency order and contains documented columns, NOT NULL constraints, primary keys, proposed foreign keys, proposed unique constraints, candidate indexes, and review comments.

Identity-versus-sequence generation remains TBD; the script contains non-executable placeholders for that decision.

The script does not connect to Oracle, execute DDL, add JDBC, implement PL/SQL, replace mock repositories, or modify application code.

### Stage 4 — Master and Reference Data Seed Design

**Status: MASTER/REFERENCE DATA SEED DESIGN COMPLETE — review only; not executed.**

Generated development/test seed script:

`database/seed/001_epic08_stage4_master_reference_seed.sql`

The script preserves mock values and seeds only confidently established records in parent-before-dependent order:

- SUPPLIER
- MERCH_GROUP
- DISTRIBUTION_CENTER
- ARTICLE
- ORDER_HEADER
- ORDER_LINE
- STOCK_ASSIGNMENT

The script intentionally leaves seed data for APP_USER, STORE, SUPPLIER_ASSIGNMENT, STORE_STOCK, DC_STOCK, STEERING_PARAMETER, and AUDIT_LOG as TBD because the required values or relationships have not been established.

The script does not connect to Oracle, execute SQL, commit data, add JDBC, implement PL/SQL, replace mock repositories, or modify application code.

### Stage 5 — JDBC Configuration and Oracle Connectivity

**Status: JDBC CONFIGURATION AND ORACLE CONNECTIVITY COMPLETE — opt-in validation only.**

Implemented infrastructure:

- Oracle JDBC driver dependency in build.sbt.
- OracleDatabaseSettings for JVM property and environment-variable configuration.
- OracleConnectionProvider for reusable connection creation and validation.
- OracleConnectionSmokeTest for explicit connectivity validation.
- Configuration/provider tests that do not require a live Oracle instance.

Configuration uses JVM properties first and environment variables as fallback:

| JVM property | Environment variable |
|---|---|
| ufm.oracle.jdbc.url | UFM_ORACLE_JDBC_URL |
| ufm.oracle.jdbc.username | UFM_ORACLE_JDBC_USERNAME |
| ufm.oracle.jdbc.password | UFM_ORACLE_JDBC_PASSWORD |

Oracle connectivity is not required for normal application startup. At this stage, mock repositories remain the active application data source. Stage 5 does not add Oracle repositories, application JDBC data access, PL/SQL, DDL execution, or seed execution. Connection failure messages do not expose the configured password.

When all three settings are explicitly configured, connectivity can be checked with:

```powershell
sbt "runMain com.metro.ufm.database.OracleConnectionSmokeTest"