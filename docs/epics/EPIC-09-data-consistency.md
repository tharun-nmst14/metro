
## EPIC-09 — Data Consistency

 
# EPIC-09: Data Consistency

## 1. Objective

Define the data consistency and SQL safety expectations that future data-changing operations in Metro UFM must follow.

This epic establishes requirements for transaction boundaries, atomic operations, commit and rollback behavior, critical data-integrity rules, and parameterized SQL. It documents the expected safeguards before the relevant write operations are implemented.

## 2. Scope

This epic covers:

- Transaction boundaries for operations that change data.
- Atomicity expectations for multi-step operations.
- Commit and rollback behavior.
- Critical data-integrity rules.
- Safe SQL construction using parameterized statements.
- Failure handling expectations for data-changing operations.

This epic defines requirements and expectations. It does not claim that all current application operations already implement these safeguards.

## 3. User Stories

### STORY-09.01: Define Transaction Handling Expectations

**Description:**

As a developer, I want clear transaction-handling requirements so that related data changes are performed consistently.

**Requirements:**

- Identify operations that involve multiple related database changes.
- Define where an explicit transaction boundary is required.
- Specify when a transaction should commit successfully.
- Specify how transaction failures should be reported.
- Ensure transaction handling is appropriate to the operation and repository boundary.

**Acceptance Criteria:**

- Transaction expectations are documented for relevant multi-step operations.
- Operations requiring atomic behavior use explicit transaction boundaries.
- Successful operations commit only after their required changes complete.
- Failed operations follow the documented rollback behavior.

**Status:** Planned

### STORY-09.02: Define Rollback Behavior for Failed Operations

**Description:**

As a developer, I want failed data-changing operations to roll back their incomplete work so that the database is not left in a partially updated state.

**Requirements:**

- Define failure conditions that require rollback.
- Specify rollback expectations when one step of a multi-step operation fails.
- Define how failures are propagated or reported to the calling service.
- Avoid treating partially completed operations as successful.
- Document any operation-specific recovery requirements.

**Acceptance Criteria:**

- Failed multi-step operations do not leave partial updates when atomic behavior is required.
- Rollback expectations are documented.
- Errors are propagated or reported without exposing credentials or other secrets.
- Success is reported only when the required operation completes successfully.

**Status:** Planned

### STORY-09.03: Define SQL Safety Expectations

**Description:**

As a developer, I want SQL statements to use parameterized inputs so that user-provided values cannot alter SQL command structure.

**Requirements:**

- Use parameterized SQL, such as JDBC PreparedStatement, for variable values.
- Never concatenate user input directly into SQL statements.
- Keep SQL command structure separate from user-supplied values.
- Review any dynamic SQL requirement separately and constrain it to approved, validated identifiers or clauses; bind parameters should be used for values.
- Document SQL safety expectations for future repositories and data-changing operations.

**Acceptance Criteria:**

- User input is never concatenated into SQL.
- Variable values are passed through SQL parameters.
- SQL safety expectations are documented for repository implementations.
- Any dynamic SQL is reviewed and constrained rather than built from untrusted input.

**Status:** Planned

### STORY-09.04: Define Critical Data-Integrity Rules

**Description:**

As a developer, I want critical data-integrity rules documented so that future operations preserve valid relationships and consistent values.

**Requirements:**

- Identify critical business invariants for each data-changing operation.
- Determine which rules should be enforced by database constraints and which require application-level validation.
- Define how related records must remain consistent during multi-step operations.
- Document required handling for constraint violations.
- Leave business rules marked TBD when requirements do not establish the expected behavior.

**Acceptance Criteria:**

- Critical integrity rules are documented before implementation.
- Multi-step changes preserve the required relationships and invariants.
- Constraint failures are handled without falsely reporting success.
- Unconfirmed business rules are explicitly marked TBD.

**Status:** Planned

## 4. Core Consistency Rules

### 4.1 Transaction Boundaries

Operations that update multiple related records must use an explicit transaction boundary when those changes must succeed or fail together.

The transaction should cover the complete logical operation, rather than allowing individual steps to commit independently.

### 4.2 Atomic Operations

When an operation is defined as atomic, either all required changes succeed or none of its changes remain applied.

Atomicity requirements must be identified for each multi-step operation before implementation.

### 4.3 Commit Expectations

- Commit only after all required steps of the logical operation complete successfully.
- Do not report success before the required commit succeeds.
- Avoid committing intermediate states when the operation requires all-or-nothing behavior.

### 4.4 Rollback Expectations

- Roll back an active transaction when a required step fails.
- Propagate or report the failure to the appropriate calling layer.
- Do not silently continue after a failure that invalidates the logical operation.
- Do not claim that rollback restores external side effects unless those effects are separately handled.

### 4.5 Critical Integrity Rules

- Preserve required relationships between related records.
- Respect approved primary-key, foreign-key, uniqueness, and check constraints.
- Validate business invariants at the appropriate application or database layer.
- Do not invent business rules where requirements remain unresolved; mark them TBD.

### 4.6 Parameterized SQL

- Use JDBC PreparedStatement or an equivalent parameterized mechanism for variable values.
- Never concatenate user input into SQL text.
- Do not treat bind parameters as a substitute for validating dynamic table names, column names, or SQL clauses.
- Keep database credentials out of SQL error messages and application logs.

## 5. Proposed Failure Handling Flow

The following flow describes the expected behavior for an operation requiring atomic database changes:

1. Begin the transaction.
2. Execute the required database operations using parameterized SQL.
3. Validate that all required steps completed successfully.
4. Commit the transaction if the logical operation succeeds.
5. If a required step fails, roll back the active transaction.
6. Propagate or report the failure without presenting the operation as successful.

The exact transaction owner, exception types, retry behavior, and user-facing messages must be defined for each operation during implementation design.

## 6. Acceptance Criteria Summary

| ID | Requirement | Expected Outcome |
|---|---|---|
| AC-09.01 | Transaction boundaries | Multi-step operations use explicit transaction boundaries where required. |
| AC-09.02 | Atomicity | Related changes either complete together or are rolled back when atomic behavior is required. |
| AC-09.03 | Commit | Commit occurs only after required steps succeed. |
| AC-09.04 | Rollback | Failed operations do not leave partial updates when rollback is required. |
| AC-09.05 | SQL safety | User input is never concatenated into SQL. |
| AC-09.06 | Data integrity | Critical constraints and business invariants are documented and preserved. |
| AC-09.07 | Failure reporting | Failures are reported without falsely indicating success or exposing secrets. |

## 7. Dependencies

- EPIC-08: Database and PL/SQL Integration

## 8. Technical Constraints

- This epic defines consistency requirements for future data-changing operations.
- It does not assert that all existing read-only repository methods require explicit transactions.
- Transaction scope must match the logical operation and the database changes that must succeed or fail together.
- Repository implementations should use parameterized SQL for variable values.
- Business rules, retry policies, isolation levels, locking strategy, and recovery behavior remain TBD unless specified by approved requirements.
- Database scripts and application code must not be changed solely on the assumption that a proposed rule has already been approved.

## 9. Risks and Considerations

- Missing transaction boundaries can leave related records inconsistent after a partial failure.
- Premature commits can make an operation impossible to roll back as a unit.
- Unhandled exceptions can conceal database failures.
- SQL concatenation can expose the application to SQL injection.
- Overly broad transactions can hold locks longer than necessary.
- Automatic retries may duplicate changes unless the operation is designed to be retry-safe.
- Application and database constraints should be coordinated so that integrity rules are enforced consistently.

## 10. Definition of Done

- [ ] Transaction handling expectations are documented.
- [ ] Multi-step operations requiring atomic behavior are identified.
- [ ] Commit conditions are documented.
- [ ] Rollback behavior for failed operations is defined.
- [ ] Critical integrity rules are documented or marked TBD.
- [ ] Parameterized SQL requirements are documented.
- [ ] Failure-reporting expectations are defined.
- [ ] Requirements are reviewed before implementation begins.
- [ ] No implementation is claimed complete without code review and appropriate tests.

 