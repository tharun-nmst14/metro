# EPIC-10: Testing

## 1. Objective

Define the testing strategy for the Metro UFM project before new test implementation begins.

This epic documents the test categories, acceptance-testing expectations, regression-testing expectations, and database-related test strategy. It establishes a consistent approach to verifying application behavior across the Scala codebase, Apache Wicket UI, services, repositories, and Oracle integration.

This document describes the testing approach only. It does not claim that tests have been implemented or executed.

## 2. Scope

This epic covers:

- Scala unit-testing strategy.
- Apache Wicket-related testing expectations.
- Service-layer and repository-layer testing.
- Database-related testing strategy.
- Acceptance-testing expectations.
- Regression-testing expectations.
- Separation of tests that require Oracle from tests that can run without a live database.

## 3. User Stories

### STORY-10.01: Define Test Categories

**Description:**

As a developer, I want the project's test categories documented so that each layer can be tested at an appropriate level.

**Requirements:**

- Define unit tests for isolated Scala classes and functions.
- Define service tests for business behavior and validation.
- Define repository tests for data-access behavior and model mapping.
- Define Wicket-related tests for page rendering, component behavior, and user interactions where practical.
- Define database integration tests for behavior that requires Oracle.
- Identify which tests can run without external infrastructure.

**Acceptance Criteria:**

- Test categories and their purposes are documented.
- Each major application layer has an appropriate testing approach.
- Oracle-dependent tests are identified separately from database-independent tests.
- The strategy does not require all tests to use a live Oracle instance.

**Status:** Planned

### STORY-10.02: Define Acceptance Testing Expectations

**Description:**

As a project stakeholder, I want acceptance-testing expectations documented so that completed functionality can be checked against its requirements.

**Requirements:**

- Define how acceptance criteria from each epic are used to guide testing.
- Identify expected user workflows for login, application navigation, DSD Order, Stock Assignment, and Steering Parameter as those features become testable.
- Define expected behavior for successful and unsuccessful user interactions.
- Require test results to be recorded for relevant acceptance checks.

**Acceptance Criteria:**

- Acceptance-testing expectations are documented.
- Tests can be traced to relevant requirements or acceptance criteria.
- Expected behavior for key user workflows is identified.
- Completion is not declared solely because code compiles.

**Status:** Planned

### STORY-10.03: Define Regression Testing Expectations

**Description:**

As a developer, I want regression-testing expectations documented so that changes do not unintentionally break existing functionality.

**Requirements:**

- Identify core application behavior that should be checked after relevant changes.
- Define when regression tests should be run.
- Include build and test execution in the verification workflow.
- Require defects found during regression testing to be recorded and addressed.

**Acceptance Criteria:**

- Regression-testing expectations are documented.
- Relevant existing tests are rerun after changes that may affect their behavior.
- Test failures are investigated rather than ignored.
- Regression results are recorded where appropriate.

**Status:** Planned

### STORY-10.04: Define Database-Related Test Strategy

**Description:**

As a developer, I want database-testing expectations documented so that Oracle-dependent behavior can be verified without making the entire test suite dependent on a live database.

**Requirements:**

- Separate database-independent tests from Oracle integration tests.
- Define configuration requirements for Oracle-dependent tests.
- Define expected behavior when Oracle integration-test configuration is unavailable.
- Ensure configured integration tests report genuine connection or query failures.
- Avoid using mock fallback behavior to conceal a failed Oracle integration test.

**Acceptance Criteria:**

- Database-related test categories are documented.
- Oracle integration tests are identifiable and explicitly configured.
- Database-independent tests can run without a live Oracle instance.
- When an Oracle integration test is enabled, connection or query failures are reported as failures.
- Test configuration does not expose database credentials.

**Status:** Planned

## 4. Test Categories

### 4.1 Scala Unit Tests

**Purpose:** Verify isolated Scala classes, methods, transformations, and validation rules.

**Expectations:**

- Test normal, boundary, and invalid-input cases.
- Keep tests independent of Oracle and other external services where practical.
- Use deterministic test inputs and assertions.
- Avoid unnecessary dependence on application startup.

### 4.2 Service Tests

**Purpose:** Verify service-layer behavior independently of the UI and, where practical, independently of the concrete repository implementation.

**Expectations:**

- Verify search and filtering behavior.
- Verify input validation and service-level rules.
- Verify handling of empty results and repository failures where applicable.
- Use controlled test dependencies when isolation is required.

### 4.3 Repository Tests

**Purpose:** Verify data-access behavior, query results, and mapping between database records and application models.

**Expectations:**

- Verify column-to-model mapping.
- Verify result ordering when the repository contract requires it.
- Verify empty-result behavior.
- Verify SQL and resource-handling failure paths where practical.
- Separate tests that can use controlled dependencies from tests requiring Oracle.

### 4.4 Apache Wicket Tests

**Purpose:** Verify page rendering, component wiring, and user interactions where supported by the project's testing setup.

**Expectations:**

- Verify that relevant pages can render.
- Verify that required components are present.
- Verify form validation and user interaction behavior.
- Verify navigation and feedback messages where practical.
- Investigate Wicket component hierarchy, markup ID, and Ajax update issues through appropriate tests or reproducible checks.

### 4.5 Database Integration Tests

**Purpose:** Verify behavior against a configured Oracle database.

**Expectations:**

- Require explicit Oracle connection configuration.
- Use a known, controlled test schema and test data.
- Verify actual SQL execution and database-to-model mapping.
- Fail when configured integration tests encounter connection or query errors.
- Avoid silently substituting mock data for a failed database integration.

### 4.6 Acceptance Tests

**Purpose:** Confirm that implemented functionality satisfies its documented requirements.

**Expectations:**

- Derive checks from epic and story acceptance criteria.
- Cover the expected user workflow and relevant failure conditions.
- Record actual results and unresolved issues.
- Do not mark requirements complete without appropriate evidence.

### 4.7 Regression Tests

**Purpose:** Detect unintended changes to previously verified functionality.

**Expectations:**

- Rerun relevant existing tests after code changes.
- Include build and test checks in the development workflow.
- Prioritize affected modules and shared components.
- Record and investigate failures.

## 5. Database Testing Strategy

The project should distinguish between the following categories:

| Category | Requires Oracle? | Purpose |
|---|---|---|
| Scala unit tests | No | Verify isolated logic |
| Service tests with controlled dependencies | No, where dependencies permit | Verify service behavior |
| Wicket tests | Not inherently | Verify page and component behavior |
| Repository tests using controlled dependencies | Not necessarily | Verify repository contracts and error handling |
| Oracle integration tests | Yes | Verify actual Oracle connectivity, SQL, and mappings |
| Acceptance tests | Depends on the workflow | Verify requirements against implemented behavior |
| Regression tests | Depends on the affected functionality | Detect unintended behavior changes |

### Oracle Integration Test Rules

- Oracle-dependent tests must be explicitly identifiable.
- Connection settings must be supplied through supported configuration.
- Database credentials must not be hard-coded in tests or committed to source control.
- If Oracle configuration is absent, an opt-in integration test may be skipped or canceled according to the test framework's conventions.
- If Oracle configuration is present and the integration test runs, connection and query failures must fail that test.
- Mock repositories must not silently replace Oracle repositories inside a test intended to verify Oracle integration.
- Test data must be controlled so that results are predictable and repeatable.
- Tests that modify database state must use an approved isolation and cleanup strategy.

The exact test framework, Oracle test schema, and cleanup approach must follow the project's implementation and approved environment.

## 6. Acceptance Testing Expectations

Acceptance tests should be derived from the relevant epic and story acceptance criteria.

Examples of areas to verify when implemented include:

- Login success and failure behavior.
- Navigation to the functions included in the approved scope.
- DSD Order filter and supplier-selection behavior.
- Stock Assignment filtering and result presentation.
- Steering Parameter screen behavior.
- Oracle supplier lookup when database integration is configured.
- Expected handling of empty results and failures.

These are candidate acceptance areas, not evidence that corresponding tests already exist.

## 7. Regression Testing Expectations

Regression testing should be performed after changes that may affect existing behavior, including:

- Shared application-shell or navigation changes.
- Wicket page, form, component, or Ajax behavior changes.
- Service or repository changes.
- Oracle connection or configuration changes.
- SQL query or model-mapping changes.
- Transaction or data-integrity changes.
- Build and dependency changes.

A regression check should include relevant automated tests and any necessary reproducible manual verification.

## 8. Suggested Verification Workflow

The following workflow is a proposed approach and must be adapted to the actual build and test setup:

1. Compile the project.
2. Run database-independent automated tests.
3. Run tests relevant to the changed module.
4. Run Wicket-related tests where available.
5. Run Oracle integration tests when the database environment is configured and the change requires them.
6. Perform acceptance checks for the affected requirements.
7. Review regression results and investigate failures.
8. Record the verification outcome before marking the work complete.

Example SBT commands, subject to the project's actual test configuration:

```powershell
sbt compile
sbt test