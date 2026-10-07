## Stage 4 Status

MASTER/REFERENCE DATA SEED DESIGN COMPLETE - review only; not executed.

The generated development/test seed script is:

`database/seed/001_epic08_stage4_master_reference_seed.sql`

It preserves mock values and seeds only confidently established records in
parent-before-dependent order:

- `SUPPLIER`
- `MERCH_GROUP`
- `DISTRIBUTION_CENTER`
- `ARTICLE`
- `ORDER_HEADER`
- `ORDER_LINE`
- `STOCK_ASSIGNMENT`

# EPIC-08 Database and PL/SQL Integration

## Stage 2 Status

SCHEMA DESIGN PROPOSED - implementation deferred.

This stage defines the proposed Oracle relational schema for the requirement-listed entities. It does not execute DDL, connect to Oracle, add JDBC, create PL/SQL, or modify application code.

## Architecture

Current:

```text
Wicket Page
   -> Service
   -> Repository
   -> Mock Data
```

Target:

```text
Wicket Page
   -> Service
   -> Repository Interface
   -> Oracle Repository
   -> JDBC
   -> Oracle Database
```

## Proposed Tables

The proposal covers:

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

These names originate from the requirements' possible core-table list. They are proposed physical entities, not confirmed existing tables.

## Design Rules

- Surrogate numeric primary keys are proposed; identity versus sequence remains TBD.
- Business numbers/codes receive unique constraints only where uniqueness is approved.
- Foreign keys are proposed for relationships supported by the domain direction.
- Indexes are candidates based on lookup/join fields; final indexes require query and volume evidence.
- Any field whose type, unit, nullability, or business meaning is not supported by the requirements is marked TBD in `docs/06-database-design.md`.
- Existing mock repositories are not replaced.

## Stage 3 Status

DDL GENERATION COMPLETE - review only; not executed.

The generated Oracle DDL script is:

`database/ddl/001_epic08_stage3_schema.sql`

It creates the 14 proposed entities in dependency order and contains the
documented columns, `NOT NULL` constraints, primary keys, proposed foreign
keys, proposed unique constraints, candidate indexes, and review comments.
Identity-versus-sequence generation remains TBD, so the script contains only
non-executable placeholders for that decision. The script does not connect to
Oracle, execute DDL, add JDBC, implement PL/SQL, replace mock repositories, or
modify application code.

## Stage 4 Status

MASTER/REFERENCE DATA SEED DESIGN COMPLETE - review only; not executed.

The generated development/test seed script is:

`database/seed/001_epic08_stage4_master_reference_seed.sql`

It preserves mock values and seeds only confidently established records in
parent-before-dependent order:

- `SUPPLIER`
- `MERCH_GROUP`
- `DISTRIBUTION_CENTER`
- `ARTICLE`
- `ORDER_HEADER`
- `ORDER_LINE`
- `STOCK_ASSIGNMENT`

The script intentionally leaves `APP_USER`, `STORE`, `SUPPLIER_ASSIGNMENT`,
`STORE_STOCK`, `DC_STOCK`, `STEERING_PARAMETER`, and `AUDIT_LOG` seed data as
TBD because their required values or relationships are not established. It
does not connect to Oracle, execute SQL, commit data, add JDBC, implement
PL/SQL, replace mock repositories, or modify application code.

## Stage 5 Status

JDBC CONFIGURATION AND ORACLE CONNECTIVITY COMPLETE - opt-in validation only.

Implemented infrastructure:

- Oracle JDBC driver dependency in `build.sbt`.
- `OracleDatabaseSettings` for property/environment configuration.
- `OracleConnectionProvider` for reusable connection creation and validation.
- `OracleConnectionSmokeTest` for explicit connectivity validation.
- Configuration/provider tests that do not require a live Oracle instance.

Configuration uses JVM properties first and environment variables as fallback:

| JVM property | Environment variable |
|---|---|
| `ufm.oracle.jdbc.url` | `UFM_ORACLE_JDBC_URL` |
| `ufm.oracle.jdbc.username` | `UFM_ORACLE_JDBC_USERNAME` |
| `ufm.oracle.jdbc.password` | `UFM_ORACLE_JDBC_PASSWORD` |

Oracle connectivity is not required for normal application startup. Existing
mock repositories remain the active application data source. The Stage 5
implementation does not add Oracle repositories, JDBC data access, PL/SQL, DDL
execution, or seed execution. Connection failure messages do not expose the
configured password.

Validation command, when all three settings are explicitly configured:

`sbt "runMain com.metro.ufm.database.OracleConnectionSmokeTest"`

## Stage 6B Status

ORACLE SUPPLIER REPOSITORY INTEGRATION VERIFICATION COMPLETE - opt-in only.

Added `OracleSupplierRepositoryIntegrationSpec`, which uses the existing
`OracleDatabaseSettings` and `OracleConnectionProvider` to call
`OracleSupplierRepository.findAll()` against the Oracle `SUPPLIER` table. The
test verifies five returned suppliers and these seeded mappings:

- `1049` -> `YEX BV`
- `1053` -> `TENFOOD BV`
- `1068` -> `PHU IMPORT EXPORT`

When Oracle configuration is unavailable, the test is canceled cleanly and the
normal test suite remains Oracle-independent. When configuration is present,
connection or query failures fail the integration test; no fallback-to-mock
behavior is used.

Opt-in PowerShell command:

```powershell
$env:UFM_ORACLE_JDBC_URL = "<oracle-jdbc-url>"
$env:UFM_ORACLE_JDBC_USERNAME = "<oracle-username>"
$env:UFM_ORACLE_JDBC_PASSWORD = "<oracle-password>"
sbt -batch "testOnly com.metro.ufm.repositories.OracleSupplierRepositoryIntegrationSpec"
```

## Stage 6C Status

SWITCH SUPPLIER SERVICE TO ORACLE REPOSITORY COMPLETE - no fallback.

`DsdOrderPage` now wires `SupplierService` to
`OracleSupplierRepository(OracleConnectionProvider.fromEnvironment())` for
normal runtime supplier lookup. `SupplierService`, `SupplierRepository`, and
the supplier model remain unchanged.

`MockSupplierRepository` remains unchanged and available for tests/development,
but is no longer selected by the application wiring for DSD supplier lookup.
No automatic Oracle-to-mock fallback was added. DSD article lookup and all
other completed functionality remain unchanged.

## Stage 6A Status

ORACLE SUPPLIER REPOSITORY COMPLETE - implementation isolated; mock remains active.

Added `OracleSupplierRepository`, which implements the existing
`SupplierRepository.findAll()` contract. It uses `OracleConnectionProvider`, a
JDBC `PreparedStatement`, and managed `Connection`, `PreparedStatement`, and
`ResultSet` resources to query:

`SELECT SUPPLIER_NO, SUPPLIER_NAME FROM SUPPLIER ORDER BY SUPPLIER_NO`

The `SUPPLIER_NO` column maps to `Supplier.number` and `SUPPLIER_NAME` maps to
`Supplier.name`. SQL failures are wrapped in a password-safe
`SupplierRepositoryException`. `SupplierService` continues to provide its
existing in-memory search filtering above the repository boundary.

`MockSupplierRepository` remains unchanged and active. No application wiring
switch, fallback selection, DSD Order, Stock Assignment, Login, PL/SQL, DDL,
or seed execution was added.

## Logical Model Mapping

- `Supplier` maps to a proposed `SUPPLIER` master-data read projection.
- `DsdOrderArticle` maps to a proposed joined order/article/supplier/stock read projection, not necessarily a single table.
- `StockAssignmentResult` maps to a proposed Stock Assignment read projection assembled from assignment, article, stock, and master data.
- `StockAssignmentDcDetail` maps to a proposed DC/stock detail read projection; its complete relational identity remains TBD.

## Stable Integration Contracts

The current repository interfaces remain the replacement boundary:

```scala
trait SupplierRepository {
  def findAll(): Seq[Supplier]
}

trait DsdOrderArticleRepository {
  def findAll(): Seq[DsdOrderArticle]
}

trait StockAssignmentRepository {
  def findAll(): Seq[StockAssignmentResult]
  def findDcDetails(): Seq[StockAssignmentDcDetail]
}
```

Future Oracle repository classes should implement these interfaces. Services and Wicket pages remain unchanged by the schema design.

## Relationships Proposed

- Orders reference suppliers and stores.
- Order lines reference order headers and articles.
- Supplier assignments reference suppliers, articles, and possibly stores.
- Store stock references stores and articles.
- DC stock references distribution centers and articles.
- Stock assignments reference articles and possibly stores/DCs.
- Articles may reference merchandise groups.
- Audit events may reference application users.

Optionality, cardinality, delete behavior, natural keys, and effective dating remain TBD.

## Deferred Implementation

Not included in Stage 2:

- DDL scripts or migrations.
- Oracle connections.
- JDBC dependencies/configuration.
- Oracle repository classes.
- PL/SQL packages, procedures, functions, or triggers.
- Database calculations or new business rules.
- Transaction implementation.

## Unresolved Questions

- Which proposed tables are needed for the first release?
- Which model fields are persisted, calculated, or display-only?
- What are the exact Oracle types, lengths, units, keys, and constraints?
- Which result projections should be views, SQL queries, or PL/SQL cursors?
- Which operations eventually write data and require transactions?
- What authentication, audit, retention, locking, and retry rules apply?

## Acceptance Criteria

- Proposed schema covers all 14 documented entities.
- Each proposed entity identifies candidate columns, types, nullability, keys, relationships, and indexes.
- Confirmed, proposed, and TBD decisions are explicitly separated.
- Existing Page -> Service -> Repository architecture is preserved.
- Application code and mock repositories remain unchanged.
