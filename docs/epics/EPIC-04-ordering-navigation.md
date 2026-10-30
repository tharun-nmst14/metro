# EPIC-04: Ordering Navigation

## 1. Objective

Implement navigation for the in-scope Ordering functions within the Metro UFM application.

This epic establishes access to **DSD Order** and **Stock Assignment** through the existing application navigation structure.

The implementation must follow the common application shell established in EPIC-03 and must not introduce navigation entries for out-of-scope Ordering functions.

## 2. Scope

The current Ordering scope includes:

- DSD Order navigation
- Stock Assignment navigation
- Ordering menu organization
- Navigation links to the in-scope pages
- Integration with the common application shell
- Navigation behavior for the two in-scope functions

## 3. Out of Scope

The following Ordering functions are excluded from this epic:

- Store Quantities
- Self Service Stock Orders
- Supplier Assignment
- Stock Order
- Stock Order Corr
- Order Maintenance
- File Upload
- Any other Ordering function not explicitly included in the current scope

These functions must not be added to the Ordering navigation as part of this epic.

## 4. User Stories

### STORY-04.01: Define Ordering Menu Entries

**Description:**

As an authenticated user, I want to access the supported Ordering functions through a clearly organized navigation menu so that I can navigate to the functionality included in the current project scope.

**Implementation Tasks:**
- Add or update the Ordering menu in the existing navigation panel.
- Include DSD Order as a navigation entry.
- Include Stock Assignment as a navigation entry.
- Follow the existing application navigation and styling conventions.
- Exclude all out-of-scope Ordering functions.

**Acceptance Criteria:**
- The Ordering menu contains DSD Order and Stock Assignment only.
- Both entries are clearly identifiable.
- Existing application navigation styling is preserved.
- No out-of-scope Ordering entries are introduced.

**Status:** Planned

### STORY-04.02: Implement DSD Order Navigation

**Description:**

As an authenticated user, I want to select DSD Order from the Ordering menu so that I can access the DSD Order page.

**Implementation Tasks:**
- Define the navigation route for DSD Order.
- Connect the menu entry to the intended Wicket page.
- Preserve the common application shell.
- Handle navigation using the existing application routing conventions.

**Acceptance Criteria:**
- Selecting DSD Order opens the intended page.
- The page is accessible through the Ordering menu.
- The common application shell remains consistent.
- The navigation entry does not redirect to an unrelated function.

**Status:** Planned

### STORY-04.03: Implement Stock Assignment Navigation

**Description:**

As an authenticated user, I want to select Stock Assignment from the Ordering menu so that I can access the Stock Assignment page.

**Implementation Tasks:**
- Define the navigation route for Stock Assignment.
- Connect the menu entry to the intended Wicket page.
- Preserve the common application shell.
- Follow the existing application routing conventions.

**Acceptance Criteria:**
- Selecting Stock Assignment opens the intended page.
- The page is accessible through the Ordering menu.
- The common application shell remains consistent.
- The navigation entry does not redirect to an unrelated function.

**Status:** Planned

## 5. Navigation Structure

The required navigation structure is:

```text
Application Shell
└── Ordering
    ├── DSD Order
    └── Stock Assignment
```

Only these two functions are included in the current Ordering scope.

## 6. Dependencies

- EPIC-01: Foundation
- EPIC-02: Login
- EPIC-03: Application Shell
- Existing Apache Wicket navigation infrastructure

## 7. Technical Considerations

- Reuse the existing navigation panel rather than creating a separate navigation framework.
- Use the project's established Wicket page and route conventions.
- Ensure component IDs match their corresponding markup IDs.
- Preserve authentication and session behavior.
- Do not create placeholder navigation entries for excluded functions.
- Keep navigation configuration maintainable as the project evolves.

## 8. Verification Steps

| Test | Expected Result |
|---|---|
| Open the application shell | Ordering menu is available |
| Expand Ordering | DSD Order and Stock Assignment are displayed |
| Inspect menu entries | No out-of-scope Ordering functions appear |
| Select DSD Order | Intended DSD Order page opens |
| Select Stock Assignment | Intended Stock Assignment page opens |
| Navigate between the two pages | Common application shell remains consistent |
| Build the project | No compilation errors are introduced |

Run the existing build and tests when appropriate:

```powershell
sbt compile
sbt test
```

Record the actual verification results after executing the checks.

## 9. Risks and Constraints

- Incorrect routes may open the wrong page or produce Wicket navigation errors.
- Navigation changes must not break the existing application shell.
- Adding excluded Ordering functions would violate the current scope.
- Navigation must remain consistent with the existing authentication workflow.

## 10. Definition of Done

- [ ] Ordering menu contains DSD Order and Stock Assignment only.
- [ ] DSD Order navigation is implemented and verified.
- [ ] Stock Assignment navigation is implemented and verified.
- [ ] Out-of-scope Ordering functions are excluded.
- [ ] Common application shell integration is preserved.
- [ ] Compilation and relevant tests pass.
- [ ] Navigation behavior is documented.

 