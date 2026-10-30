# EPIC-03: Application Shell

## 1. Objective

Implement a consistent, reusable application shell for the Metro UFM enterprise-style demo.

The shell provides common visual elements and navigation across the application, including METRO branding, the header, navigation menu, user information, application version, date/time, and contextual information.

The shell integrates with the login and session-management functionality established in EPIC-02.

## 2. Scope

- Common application header
- METRO branding
- Application navigation
- User information display
- Application version display
- Date and time display
- Context bar
- Shared page layout
- Reusable Apache Wicket panels
- Shell styling and resource organization
- Integration with login and logout workflows

## 3. Out of Scope

- Login authentication logic
- Database-backed user profiles
- Role-based navigation permissions
- Individual business module implementations
- Store Quantities business logic
- Oracle database integration
- Administrative user management
- Advanced personalization and dashboard configuration

These capabilities are managed through their respective epics.

## 4. User Stories

### STORY-03.01: Implement the Common Application Header

**Description:**

As an authenticated user, I want a consistent application header so that I can identify the Metro UFM application and access common application information.

**Implementation Tasks:**
- Implement the reusable header component.
- Display METRO branding.
- Display the current page or module title where supported.
- Apply consistent header styling.
- Integrate the header into the application shell.

**Acceptance Criteria:**
- The application header is displayed on the relevant application pages.
- METRO branding is visible.
- The current page or module context is displayed where configured.
- Header styling remains consistent across pages.
- Shared header functionality is maintained in a reusable component.

**Status:** Completed

### STORY-03.02: Implement Application Navigation

**Description:**

As an authenticated user, I want a consistent navigation menu so that I can access the available application modules.

**Implementation Tasks:**
- Implement the navigation panel.
- Organize navigation entries by module.
- Support the existing Ordering navigation structure.
- Provide navigation links for implemented modules.
- Maintain consistent navigation styling.

**Acceptance Criteria:**
- Navigation is displayed within the application shell.
- Available navigation entries are accessible.
- Existing module navigation is preserved.
- Navigation links route to their intended pages.
- Navigation remains consistent across the application.

**Status:** Completed

### STORY-03.03: Display User Information

**Description:**

As an authenticated user, I want to see my current user information so that I can identify the active application session.

**Implementation Tasks:**
- Implement the user information panel.
- Retrieve available user information from the existing application context.
- Display the configured username or equivalent identifier.
- Integrate the user information panel into the shell.
- Preserve the existing logout workflow.

**Acceptance Criteria:**
- User information is displayed where configured.
- Displayed information corresponds to the current application session.
- Sensitive credentials are never displayed.
- The panel integrates with the existing authentication workflow.

**Status:** Completed

### STORY-03.04: Display Application Version

**Description:**

As a developer or user, I want the application version to be available so that the running application can be identified during development and support.

**Implementation Tasks:**
- Retrieve the application version from `ApplicationSettings`.
- Display the configured version in the appropriate shell location.
- Maintain a consistent version-display format.

**Acceptance Criteria:**
- The configured application version is displayed where implemented.
- Version information is retrieved from application configuration.
- The version display does not require hardcoded values in the UI component.

**Status:** Completed

### STORY-03.05: Display Date and Time

**Description:**

As an authenticated user, I want date and time information displayed where required so that I can understand the application's current temporal context.

**Implementation Tasks:**
- Define the date/time display format.
- Implement the date/time presentation where required by the existing design.
- Ensure the displayed value follows the agreed time-zone convention.
- Maintain consistent formatting.

**Acceptance Criteria:**
- Date/time information is displayed where implemented.
- The format is consistent with the intended application design.
- Time-zone behavior is documented or configured where necessary.

**Status:** Completed

### STORY-03.06: Implement the Context Bar

**Description:**

As an authenticated user, I want a context bar so that I can identify the current application area or operational context.

**Implementation Tasks:**
- Define the context bar's location in the shell.
- Display relevant page or module context where supported.
- Maintain consistent styling and spacing.
- Integrate the context bar into the shared layout.

**Acceptance Criteria:**
- The context bar is displayed where implemented.
- Context information corresponds to the active page or module.
- The context bar is visually consistent across applicable pages.

**Status:** Completed

### STORY-03.07: Integrate the Shell with Application Pages

**Description:**

As a developer, I want a reusable shell so that application pages can share common UI components without duplicating layout logic.

**Implementation Tasks:**
- Implement the common application shell page.
- Integrate the header, navigation, and user information panels.
- Establish consistent resource loading and styling.
- Integrate the shell with successful login and logout workflows.
- Preserve existing application navigation behavior.

**Acceptance Criteria:**
- Shared shell components are integrated into the application layout.
- Common UI elements are reusable.
- Application pages preserve consistent layout behavior.
- Existing login, navigation, and logout functionality continues to work.

**Status:** Completed

## 5. Shell Component Structure

The application shell uses the following component responsibilities:

| Component | Responsibility |
|---|---|
| `ApplicationShellPage.scala` | Common application layout |
| `HeaderPanel.scala` | Header and METRO branding |
| `NavigationPanel.scala` | Application module navigation |
| `UserInfoPanel.scala` | Current user information |
| `LogoutPage.scala` | Logout workflow |
| `ApplicationSettings.scala` | Application configuration, including version |

The exact implementation and placement of the date/time and context bar should remain consistent with the existing repository and UI design.

## 6. Dependencies

- EPIC-01: Foundation
- EPIC-02: Login
- Apache Wicket page and panel infrastructure
- Application configuration
- Session and navigation infrastructure

## 7. Deliverables

- Common application shell
- Header and METRO branding
- Navigation panel
- User information panel
- Application version display
- Date/time presentation
- Context bar
- Shared layout and styling
- Integration with login and logout

## 8. Verification Steps

Verify the following scenarios in the running application:

| Test | Expected Result |
|---|---|
| Log in successfully | Application shell is displayed |
| Inspect the header | METRO branding and page context appear where configured |
| Inspect navigation | Available module links are displayed |
| Inspect user information | Current user information appears where configured |
| Inspect application version | Configured version is displayed |
| Inspect date/time | Expected date/time presentation appears where implemented |
| Navigate between modules | Common shell layout remains consistent |
| Log out | Existing logout workflow operates correctly |

Run the existing build and tests when appropriate:

```powershell
sbt compile
sbt test
```

Record verification results only after executing the relevant checks.

## 9. Risks and Notes

- Shared shell changes can affect multiple application pages.
- Wicket component IDs must match their corresponding markup identifiers.
- Navigation entries should point only to implemented or intentionally planned pages.
- User information must not expose passwords or other sensitive configuration.
- Date/time presentation should use a consistent time-zone convention.
- Shell components should remain reusable rather than being duplicated across pages.

## 10. Definition of Done

- [x] Common application shell implemented.
- [x] Header and METRO branding integrated.
- [x] Navigation panel implemented.
- [x] User information panel integrated.
- [x] Application version configuration integrated.
- [x] Date/time and context presentation addressed according to the implemented design.
- [x] Shell integrated with the login workflow.
- [x] Logout integration preserved.
- [x] Shared layout and styling established.
- [x] Verification scenarios documented.

## 11. Final Status

**Status: Completed**

The application shell provides a shared layout for the Metro UFM application, including common branding, navigation, user information, application configuration, and contextual presentation.

The shell establishes a consistent foundation for business modules and supports the existing login and logout workflows.

Feature-specific functionality remains tracked under its respective epic.