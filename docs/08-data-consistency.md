# EPIC-08 Stage 1 - Data Consistency Design

## Status

DESIGN PROPOSAL ONLY - database transactions and consistency behavior are not implemented.

## Confirmed by Requirements

The requirements state that the final database implementation should provide:

- Transactions for operations involving multiple related database changes.
- Atomic behavior where required.
- Rollback when a required operation fails.
- Referential integrity through database constraints.
- Duplicate prevention using constraints or application validation where appropriate.
- Explicit transaction boundaries.
- Parameterized JDBC queries.

These are target requirements, not current runtime behavior.

## Current Runtime Behavior

- Repositories return fixed mock data.
- DSD Order and Stock Assignment searches are in-memory filtering.
- Stock Assignment Apply changes page models and loads mock rows.
- Cancel/X discard popup draft values.
- No persistent write is performed.
- No commit, rollback, lock, or database transaction occurs.

## Proposed Transaction Ownership

For future modifying operations:

```text
Service
  -> Repository transaction boundary
  -> JDBC connection
  -> SQL and/or approved PL/SQL
  -> commit on success
  -> rollback on failure
```

This is a design direction, not an implementation decision about every operation. Exact ownership must be confirmed with the JDBC/repository design.

## Proposed Transaction Boundaries

### Read-only operations

Supplier lookup, DSD Order retrieval, Stock Assignment main result retrieval, and Stock Assignment DC detail retrieval are expected to be read-only. They do not require application-managed write transactions unless Oracle-specific consistency requirements later say otherwise.

### Future order or stock modifications

A future operation that changes multiple related order, stock, assignment, or audit records should be one atomic transaction. The exact operation and participating entities are TBD because the current UI does not implement writes.

### Future authentication persistence

If login is moved from configured mock credentials to Oracle, the authentication lookup is expected to be read-only. User-management writes, if ever added, require their own approved transaction contract.

## Integrity Design Candidates

Requirement-supported candidates:

- Primary keys for business entities.
- Foreign keys for approved relationships.
- `NOT NULL`, `UNIQUE`, `CHECK`, and referential constraints where justified.
- Indexes for frequent search/join predicates.

TBD:

- Exact constraints and columns.
- Isolation level.
- Locking strategy.
- Retry behavior.
- Idempotency requirements.
- Audit behavior.
- Cross-table consistency rules.
- Whether PL/SQL commits or participates in a caller-managed transaction.

## No Invented Rules

The current models contain display fields such as forecast, stock, quantity gap, promotion, recalculation, key distribution, and article exchange. Their business calculations and consistency rules are not defined by the current requirements and must remain TBD.

## Future Test Design

After the database contract is approved, tests should cover the defined operation cases:

- Successful read.
- Successful multi-record write, if introduced.
- Constraint or validation failure.
- Rollback after a required step fails.
- Retry/idempotency behavior where specified.
- Concurrent update behavior where specified.

Exact scenarios and expected values remain TBD.
