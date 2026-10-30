# EPIC-02: Login

## 1. Objective

Implement a login capability for the Metro UFM application that provides an entry point for users, validates submitted credentials, and establishes an authenticated application session.

The login functionality integrates with the existing Scala, Apache Wicket, and application configuration architecture.

## 2. Scope

- Login screen and user interface
- Username and password inputs
- Required-field validation
- Credential verification
- Authentication service
- Login success and failure handling
- Session-based authentication state
- Navigation after successful authentication
- Logout integration
- Configuration-based login credentials
- Authentication flow verification

## 3. Out of Scope

- Database-backed user authentication
- User registration and account creation
- Password reset and recovery
- Role-based access control
- Multi-factor authentication
- External identity provider integration
- User administration
- Persistent user account management

These capabilities may be introduced through future epics if required.

## 4. User Stories

### STORY-02.01: Display the Login Screen

**Description:**

As a user, I want a login screen so that I can enter my credentials to access the Metro UFM application.

**Implementation Tasks:**
- Implement the login page using Apache Wicket.
- Provide username and password fields.
- Add a login submission control.
- Display validation and authentication messages.
- Apply consistent application styling.

**Acceptance Criteria:**
- The login page is accessible through the application's configured entry point.
- Username and password fields are displayed.
- The password field masks the entered password.
- The login form supports submission.
- Validation messages are displayed appropriately.

**Status:** Completed

### STORY-02.02: Validate Login Inputs

**Description:**

As a user, I want the login form to validate my inputs so that incomplete submissions can be handled appropriately.

**Implementation Tasks:**
- Validate required username input.
- Validate required password input.
- Prevent authentication when required fields are missing.
- Display appropriate validation messages.

**Acceptance Criteria:**
- Empty required fields are rejected.
- Invalid submissions do not establish an authenticated session.
- Validation feedback is clear.
- The login page remains usable after validation failures.

**Status:** Completed

### STORY-02.03: Authenticate User Credentials

**Description:**

As a user, I want my submitted credentials to be verified so that only users with matching configured credentials can log in.

**Implementation Tasks:**
- Implement the authentication service.
- Load configured credentials through application settings.
- Compare submitted credentials against the configured values.
- Return an authentication result.
- Handle invalid credentials without exposing sensitive information.

**Acceptance Criteria:**
- Valid configured credentials are accepted.
- Invalid credentials are rejected.
- Authentication logic is separated from the Wicket page.
- Credentials are not exposed through UI messages or application logs.
- Authentication behavior is consistent with the current configuration model.

**Status:** Completed

### STORY-02.04: Establish an Authenticated Session

**Description:**

As an authenticated user, I want the application to retain my login state so that I can navigate between protected application pages.

**Implementation Tasks:**
- Integrate authentication with the Wicket session.
- Establish authenticated state after successful login.
- Support navigation to the application shell.
- Ensure unsuccessful authentication does not establish a session.
- Integrate logout with session handling.

**Acceptance Criteria:**
- Successful authentication establishes the expected session state.
- The user can navigate to the application shell after login.
- Failed authentication does not grant access.
- Logout clears the authenticated state.
- Session behavior is consistent with the existing application architecture.

**Status:** Completed

### STORY-02.05: Configure Login Credentials

**Description:**

As a developer, I want login configuration separated from page implementation so that credentials can be changed without modifying the login page.

**Implementation Tasks:**
- Use `ApplicationSettings.scala` for login configuration.
- Support configured username and password values.
- Support the existing environment-variable and JVM-property conventions.
- Define appropriate behavior when configuration is missing.
- Avoid exposing configured credentials.

**Acceptance Criteria:**
- Login credentials are retrieved through application settings.
- The login page does not hardcode credential values.
- Supported configuration sources are documented.
- Sensitive values are not logged or displayed.

**Status:** Completed

### STORY-02.06: Integrate Login with the Application Shell

**Description:**

As a user, I want successful authentication to lead into the application shell so that I can access the available application modules.

**Implementation Tasks:**
- Connect successful login to the application shell.
- Preserve the existing header and navigation components.
- Integrate user information display where configured.
- Provide a logout action.
- Verify navigation after login and logout.

**Acceptance Criteria:**
- Successful login leads to the expected application page.
- The existing application shell is preserved.
- Logout returns the user to the expected unauthenticated state.
- Login functionality remains compatible with the current navigation structure.

**Status:** Completed

## 5. Authentication Flow

The login flow follows this sequence:

1. The user opens the login page.
2. The user enters a username and password.
3. The application validates the submitted inputs.
4. The authentication service verifies the credentials.
5. If authentication fails, the application displays an appropriate error.
6. If authentication succeeds, the application establishes the authenticated session.
7. The user is redirected to the application shell.
8. The user can log out to end the authenticated session.

## 6. Key Components

| Component | Responsibility |
|---|---|
| `LoginPage.scala` | Login interface and submission handling |
| `AuthenticationService.scala` | Credential verification |
| `ApplicationSettings.scala` | Application configuration and login settings |
| Wicket session implementation | Authenticated session state |
| `ApplicationShellPage.scala` | Main application shell after login |
| `LogoutPage.scala` | Logout workflow |

The names above reflect the existing project components described during development. Their exact implementations should be verified against the repository.

## 7. Dependencies

- EPIC-01: Foundation
- Scala and SBT project configuration
- Apache Wicket application setup
- Application settings and configuration management
- Application shell and session infrastructure

## 8. Deliverables

- Login page
- Login input validation
- Authentication service
- Session-based authentication handling
- Application configuration integration
- Login success and failure handling
- Application shell navigation after login
- Logout functionality
- Login verification documentation

## 9. Verification Steps

Verify the following scenarios against the running application:

| Test | Expected Result |
|---|---|
| Submit empty credentials | Validation feedback appears |
| Submit an incorrect username | Authentication fails |
| Submit an incorrect password | Authentication fails |
| Submit valid configured credentials | Login succeeds |
| Navigate after successful login | Application shell is displayed |
| Log out | Authenticated state is cleared |
| Inspect error messages | Passwords and sensitive configuration are not exposed |

Run the existing project checks when appropriate:

```powershell
sbt compile
sbt test
```

Record test results only after the commands have actually been executed.

## 10. Risks and Notes

- Default development credentials must not be treated as production credentials.
- Application login credentials are separate from Oracle database credentials.
- Environment variables and JVM properties must be configured consistently.
- Session-based authentication should be reviewed before production deployment.
- Authorization for individual modules may require a separate epic.
- Database-backed authentication is outside the current scope.

## 11. Definition of Done

- [x] Login interface implemented.
- [x] Input validation implemented.
- [x] Authentication service integrated.
- [x] Application settings integrated.
- [x] Session handling implemented.
- [x] Successful login navigation implemented.
- [x] Logout functionality implemented.
- [x] Login verification scenarios documented.

## 12. Final Status

**Status: Completed**

The login capability provides the application's authentication entry point, validates credentials through the authentication service, establishes session state, and integrates with the application shell and logout workflow.

The current implementation uses configured credentials rather than database-backed user accounts. Additional authentication and authorization capabilities can be introduced through future epics.