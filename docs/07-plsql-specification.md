# EPIC-08 Stage 1 - PL/SQL Integration Design

## Status

DESIGN PROPOSAL ONLY - no PL/SQL implementation exists.

## Confirmed by Requirements

- Oracle is the final database platform.
- JDBC is the database access mechanism.
- Repository classes own database interaction.
- PL/SQL may be used where an operation is better handled inside Oracle.
- Business logic should not be unnecessarily duplicated between Scala and PL/SQL.
- Procedures, functions, packages, and justified triggers are possible PL/SQL components.

## Derived from Current Code

Current services perform mock behavior only:

- `DsdOrderArticleService.search(...)` filters `DsdOrderArticle` values.
- `SupplierService.search(...)` filters `Supplier` values.
- `StockAssignmentService.search(...)` filters supported Stock Assignment criteria.
- `StockAssignmentService.findDcDetails()` retrieves mock DC detail rows.
- `ConfiguredAuthenticationService` compares configured values and has no repository.

No current operation calls JDBC or PL/SQL.

## PL/SQL Candidates

These are candidates, not approved procedures:

### Read functions or read packages

Potentially useful only if Oracle must own a defined derivation or reusable read contract:

- Stock Assignment result derivation.
- Stock Assignment DC detail derivation.
- DSD Order article result derivation.

Current requirements do not define the calculations, so no function signature is proposed.

### Procedures

Potentially useful for future multi-step write workflows involving approved tables, for example order or stock updates. No write workflow is implemented or sufficiently specified now; procedure names, parameters, and effects are TBD.

### Packages

A package boundary may group an approved domain operation set, but package names and ownership are TBD. Possible future domains are only conceptual:

- DSD Order operations.
- Stock Assignment operations.
- Authentication operations.

These names are not implementation commitments.

### Triggers

Triggers are not currently required by the code or requirements. Use would require explicit justification, especially for audit or integrity behavior. TBD.

## Proposed Call Boundary

```text
Wicket Page
  -> Service
  -> Oracle Repository
  -> JDBC CallableStatement or PreparedStatement
  -> Approved PL/SQL contract, when required
```

The service owns application orchestration. The repository owns JDBC parameter/result mapping. PL/SQL owns only behavior explicitly assigned to Oracle by the approved contract.

## Error Contract - TBD

The future design must define:

- SQL error mapping.
- PL/SQL application error mapping.
- Validation error representation.
- Retryable versus non-retryable failures.
- Logging and correlation information.
- Transaction outcome after an exception.

No error code or exception type is invented here.

## Stable Repository Requirement

Oracle repository implementations must satisfy the current interfaces before any interface expansion is considered:

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

Any new write or authentication repository interface requires a separately approved contract.

## Deferred Details

TBD until business clarification and database design approval:

- Package/procedure/function names.
- Signatures and parameter types.
- Cursor/result-set structures.
- Validation and calculation ownership.
- Commit/rollback ownership.
- Deployment/versioning scripts.
- Test fixtures and integration environments.
