# Metro UFM Demo – Requirements Specification

**Document:** `requirements.md`  
**Project:** Metro Ultra Fresh Management (UFM) Demo  
**Version:** 1.0  
**Status:** Draft / Demo Scope  
**Technology:** Scala, SBT, Apache Wicket, Jetty, Oracle, JDBC, PL/SQL

---

# 1. Purpose

This document defines the functional and non-functional requirements for the Metro UFM Demo application.

The application is a simplified enterprise-style demo inspired by the provided Metro UFM screens and workflows.

The primary objective is to implement the following functional areas:

1. Login
2. Common Application Shell
3. Ordering
   - DSD Order
   - Stock Assignment
4. Parameter
   - Steering Parameter

The application should be implemented incrementally using an epic-based development approach.

---

# 2. Project Scope

## 2.1 In Scope

The following features are included in the current demo scope.

### Authentication

- User login
- Username and password validation
- Login success handling
- Login failure handling
- Logout

### Common Application Shell

- Metro/UFM-style header
- Navigation menu
- User information
- Application information
- Date/time display
- Module navigation
- Common page structure

### Ordering Module

The Ordering module contains the following functions:

- DSD Order
- Stock Assignment

### Parameter Module

The Parameter module currently contains:

- Steering Parameter

### Database

The application will eventually use:

- Oracle Database
- JDBC
- PL/SQL
- Transactions
- Referential integrity
- Constraints
- Indexes
- Audit information where required

---

# 3. Out of Scope

The following features are intentionally excluded from the current implementation.

## 3.1 Ordering Functions

The following are future scope:

- Store Quantities
- Self Service Stock Orders
- Supplier Assignment
- Stock Order
- Stock Order Correction
- Order Maintenance
- File Upload

## 3.2 Parameter Functions

The following are future scope:

- Order Reports
- Purchase Area
- PAXD Supplier
- Sales Lines
- DCs
- VTO Supplier
- Row Selector
- Interfaces
- Delivery DC Supplier
- DSD Supplier Whitelist

These options may be displayed as disabled/future options if required, but they must not contain functional implementation.

---

# 4. Technology Requirements

## 4.1 Backend

- Scala
- SBT
- Apache Wicket
- Jetty
- JDBC
- Oracle JDBC Driver

## 4.2 Database

- Oracle Database
- SQL
- PL/SQL

## 4.3 Frontend

- Apache Wicket HTML markup
- HTML5
- CSS
- JavaScript only where required

## 4.4 Development Tools

- Git
- GitHub
- GitHub Copilot
- IntelliJ IDEA or VS Code

---

# 5. Architecture Requirements

The application shall follow the layered architecture below.

```text
Browser
   |
   v
Wicket Pages / Panels
   |
   v
Service Layer
   |
   v
Repository Layer
   |
   v
JDBC
   |
   v
Oracle Database
   |
   v
PL/SQL
```

## 5.1 Presentation Layer

Responsible for:

- Wicket Pages
- Wicket Panels
- HTML
- CSS
- User interaction
- Validation messages

The presentation layer must not directly access the database.

---

# 6. Functional Requirements

## EPIC-01: Project Foundation

### FR-01.1 Project Creation

The application shall be created using:

- Scala
- SBT
- Apache Wicket
- Jetty

### FR-01.2 Project Structure

The project shall follow a clear package structure.

```text
com.metro.ufm
├── pages
├── panels
├── models
├── services
├── repositories
├── config
└── utils
```

### FR-01.3 Configuration

Application configuration shall be separated from business logic.

Configuration may contain:

- Database configuration
- Application settings
- Environment-specific properties

### FR-01.4 Build

The project shall be buildable using SBT.

Example:

```bash
sbt compile
```

---

## EPIC-02: Login

### FR-02.1 Login Screen

The application shall provide a login page containing:

- Username
- Password
- Login button
- Cancel/reset option

### FR-02.2 Username Validation

The system shall validate that the username is provided.

### FR-02.3 Password Validation

The system shall validate that the password is provided.

### FR-02.4 Authentication

The system shall authenticate the user against the configured authentication source.

Initially, mock/in-memory authentication may be used.

Later, authentication shall use Oracle/JDBC.

### FR-02.5 Successful Login

After successful authentication:

```text
Login
  ↓
Main Application
```

The user shall be redirected to the main application page.

### FR-02.6 Failed Login

For invalid credentials, the system shall display an appropriate error message.

Example:

```text
Invalid username or password.
```

### FR-02.7 Logout

The application shall provide a logout option.

Logout shall invalidate the user's application session.

---

## EPIC-03: Common Application Shell

### FR-03.1 Main Layout

The application shall provide a common layout containing:

- Header
- Navigation
- Content area
- User information
- Application information

### FR-03.2 Header

The header shall provide Metro/UFM-style application branding.

### FR-03.3 Navigation

The navigation shall provide access to:

```text
Ordering
Parameter
```

### FR-03.4 User Information

The application shall display the currently logged-in user where required.

### FR-03.5 Date and Time

The application may display:

- Current date
- Current time

### FR-03.6 Reusable Components

Common UI elements should be implemented as reusable Wicket Panels.

Examples:

```text
HeaderPanel
NavigationPanel
UserInfoPanel
FooterPanel
```

---

## EPIC-04: Ordering Navigation

### FR-04.1 Ordering Module

The application shall provide an Ordering module.

### FR-04.2 Ordering Functions

The current functional options are:

```text
Ordering
 ├── DSD Order
 └── Stock Assignment
```

### FR-04.3 Future Options

Future Ordering options shall not contain functional implementation.

Example:

```text
Store Quantities
Self Service Stock Orders
Supplier Assignment
Stock Order
Stock Order Corr
Order Maintenance
File Upload
```

These may be displayed as disabled or future functionality.

### FR-04.4 Navigation

Selecting:

```text
DSD Order
```

shall open the DSD Order function.

Selecting:

```text
Stock Assignment
```

shall open the Stock Assignment function.

---

## EPIC-05: DSD Order

### FR-05.1 DSD Order Page

The application shall provide a DSD Order page.

### FR-05.2 Filter Criteria

The page shall provide filtering options including:

1. PUAR
2. SL
3. SID
4. Delivery Day
5. Supplier Number
6. Supplier Name
7. Order Number
8. Article Number
9. Subsystem Number
10. Description

### FR-05.3 Supplier Selection

The system shall provide a supplier selection area.

Example suppliers for demo purposes may include:

- YEX BV
- TENFOOD BV
- PHU IMPORT EXPORT
- MEDICAL PLUS GMBH
- COCA-COLA

Demo supplier data must be treated as sample data and not as production information.

### FR-05.4 Apply Filter

The user shall be able to apply the selected filter criteria.

The system shall validate required criteria before executing the search.

### FR-05.5 Cancel Filter

The user shall be able to cancel or reset the filter.

### FR-05.6 Result Context

The result page shall display relevant context information such as:

- PUAR
- SL
- SID
- Store Name
- Supplier Number
- Supplier Name
- Delivery Day

### FR-05.7 Order Table

The result shall provide an article/order table.

The table may contain:

1. S/G
2. Article Number
3. Variant Number
4. Bundle
5. Subsystem
6. Description
7. Sorttext
8. Sizetext
9. CBB
10. BC
11. Sales Forecast
12. Stock
13. Order Information
14. Order Quantity
15. Price
16. MRP
17. Promotion

### FR-05.8 Business Rules

Exact order calculation and business rules shall be marked as:

```text
TBC – To Be Confirmed
```

until confirmed from the actual business requirements.

The application must not invent business rules.

### FR-05.9 Data Retrieval

The UI shall retrieve data through:

```text
Page
 ↓
Service
 ↓
Repository
 ↓
JDBC
 ↓
Oracle
```

The Wicket page must not execute SQL directly.

---

## EPIC-06: Stock Assignment

### FR-06.1 Stock Assignment Page

The application shall provide a Stock Assignment function under Ordering.

### FR-06.2 Filter Criteria

The filter shall support the following fields where applicable:

1. Assortment
2. DC Number
3. Assignment Day
4. Delivery Day SID
5. Cut-off Time
6. Articles with Quantity
7. Stores with Quantity
8. Quantity Gap
9. S/G Number
10. Article Number
11. Subsystem Number
12. Merch Group
13. Article Description
14. Remaining Stock

### FR-06.3 Search

The user shall be able to apply the selected filter criteria.

### FR-06.4 Reset

The user shall be able to clear/reset the selected filter criteria.

### FR-06.5 Result Table

The result table may contain:

1. S/G
2. Merch Group
3. Article Number
4. Variant
5. Bundle
6. Subsystem
7. Description
8. Sorttext
9. Sizetext
10. CBB
11. BC
12. Sales Forecast
13. Store Quantity
14. Assignment Stock Quantity
15. Recalculation
16. Key Distribution
17. Quantity Gap
18. Remaining Quantity
19. UFM Stock
20. Promotion
21. IBC
22. Article Exchange

### FR-06.6 DC Information

Where required, the result shall provide relevant Distribution Center information.

### FR-06.7 Data Access

Stock Assignment data shall be retrieved through the service and repository layers.

Direct database access from Wicket pages is prohibited.

---

## EPIC-07: Steering Parameter

### FR-07.1 Parameter Module

The application shall provide a Parameter module.

### FR-07.2 Steering Parameter

The current functional scope shall contain:

```text
Steering Parameter
```

### FR-07.3 Steering Settings

The screen shall support settings such as:

1. Saturday Order Day
2. Sunday Order Day
3. Saturday Delivery Day
4. Sunday Delivery Day
5. EDI for DSD
6. No Delivery Day on Holiday
7. Display Sales Forecast
8. Order By COMS
9. Automatic Supplier Assignment
10. Automatic Supplier Assignment – Article Level
11. Automatic Supplier Assignment – DC Level

### FR-07.4 Parameter Controls

Depending on the final UI design, settings may use:

- Checkbox
- Radio button
- Dropdown
- Text field
- Date field

### FR-07.5 Save

The user shall be able to save valid parameter changes.

### FR-07.6 Validation

Invalid parameter values shall not be saved.

### FR-07.7 Persistence

Initially, mock data may be used.

Final implementation shall persist parameter values in Oracle.

---

## EPIC-08: Database and PL/SQL

### FR-08.1 Database

The final implementation shall use Oracle Database.

### FR-08.2 Core Tables

The database design may include:

```text
APP_USER
STORE
SUPPLIER
DISTRIBUTION_CENTER
MERCH_GROUP
ARTICLE
ORDER_HEADER
ORDER_LINE
SUPPLIER_ASSIGNMENT
STORE_STOCK
DC_STOCK
STOCK_ASSIGNMENT
STEERING_PARAMETER
AUDIT_LOG
```

### FR-08.3 Primary Keys

Each business entity shall have a unique primary key.

### FR-08.4 Foreign Keys

Relationships shall be enforced using foreign keys wherever appropriate.

### FR-08.5 Constraints

The database shall use appropriate:

- `NOT NULL`
- `UNIQUE`
- `PRIMARY KEY`
- `FOREIGN KEY`
- `CHECK`

constraints.

### FR-08.6 Indexes

Indexes shall be created for frequently searched or joined columns where justified.

### FR-08.7 SQL

SQL queries shall use parameterized statements.

Example:

```sql
SELECT *
FROM supplier
WHERE supplier_no = ?
```

The application must not construct SQL using raw user input.

### FR-08.8 JDBC

Database access shall be implemented using JDBC.

Repository classes shall be responsible for database interaction.

### FR-08.9 PL/SQL

PL/SQL shall be used where business operations are better handled inside Oracle.

Possible PL/SQL components include:

- Procedures
- Functions
- Packages
- Triggers where justified

Business logic must not be unnecessarily duplicated between Scala and PL/SQL.

---

## EPIC-09: Data Consistency

### FR-09.1 Transaction Management

Operations involving multiple related database changes shall use transactions.

General flow:

```text
BEGIN
   Validate
   ↓
   Perform Operation
   ↓
   Validate Result
   ↓
   COMMIT
END
```

If an error occurs:

```text
BEGIN
   Operation
   ↓
   Error
   ↓
   ROLLBACK
END
```

### FR-09.2 Atomic Operations

A business operation that requires multiple related updates shall not leave the database partially updated.

### FR-09.3 Rollback

The system shall rollback the transaction when a required operation fails.

### FR-09.4 Referential Integrity

Invalid relationships shall be prevented using database constraints.

### FR-09.5 Duplicate Prevention

Where duplicate business data is not allowed, unique constraints or application validation shall be used.

### FR-09.6 Concurrent Updates

The system shall consider concurrent updates for critical business operations.

### FR-09.7 Silent Overwrite Prevention

The system should not silently overwrite another user's changes where concurrency matters.

---

## EPIC-10: Testing and Stabilization

### FR-10.1 Unit Testing

Services and important business logic shall have unit tests.

### FR-10.2 Repository Testing

Repository operations shall be tested against appropriate test data.

### FR-10.3 UI Testing

Important Wicket pages and user flows shall be tested.

### FR-10.4 Integration Testing

The application shall test:

```text
Wicket
   ↓
Service
   ↓
Repository
   ↓
JDBC
   ↓
Oracle
```

### FR-10.5 Transaction Testing

Transaction scenarios shall include:

- Successful transaction
- Validation failure
- Database failure
- Rollback
- Commit
- Partial operation failure

### FR-10.6 Regression Testing

Existing functionality shall be tested after each major feature addition.

---

# 7. Non-Functional Requirements

## NFR-01: Maintainability

The code shall follow a clear layered architecture.

Business logic shall not be placed directly inside Wicket HTML/page classes.

---

## NFR-02: Readability

Code shall use:

- Meaningful names
- Small methods
- Clear class responsibilities
- Appropriate comments

---

## NFR-03: Reusability

Common UI functionality should be implemented using reusable Wicket Panels.

Examples:

```text
HeaderPanel
NavigationPanel
FilterPanel
TablePanel
MessagePanel
```

---

## NFR-04: Security

The application shall:

- Validate user input
- Use parameterized SQL
- Avoid hardcoded database credentials
- Avoid exposing passwords
- Protect authenticated pages

---

## NFR-05: Database Security

Database credentials shall be stored in configuration/environment-specific mechanisms rather than source code.

---

## NFR-06: Performance

The application shall avoid:

- Unnecessary database calls
- Repeated identical queries
- Unbounded result sets
- Inefficient joins

Pagination should be considered for large result tables.

---

## NFR-07: Error Handling

Errors shall be handled at the appropriate layer.

The application should provide user-friendly messages while logging technical details appropriately.

Example user message:

```text
Unable to retrieve order information.
Please try again.
```

---

## NFR-08: Logging

Important application events should be logged.

Examples:

- Login attempt
- Login failure
- Database error
- Transaction failure
- Important business operation
- Application error

Sensitive information such as passwords must never be logged.

---

## NFR-09: Scalability

The architecture should allow future modules to be added without restructuring the entire application.

Future functions should be able to reuse:

- Layout
- Navigation
- Services
- Repository patterns
- Database infrastructure

---

## NFR-10: Deployment

The application should be runnable using SBT and Jetty.

Example:

```bash
sbt compile
```

and the configured application run command.

---

# 8. Cross-Epic Requirements

## CR-01: Layer Separation

The following separation shall be maintained:

```text
Wicket Page
     ↓
Service
     ↓
Repository
     ↓
JDBC
     ↓
Oracle
```

---

## CR-02: No Direct SQL in Wicket Pages

The following is prohibited:

```scala
class DsdOrderPage extends WebPage {
  // SQL directly inside page
}
```

SQL must be handled through repositories.

---

## CR-03: No Database Dependency During Initial UI Development

The initial UI implementation may use mock repositories.

Example:

```text
MockDsdOrderRepository
MockStockAssignmentRepository
MockSteeringParameterRepository
```

This allows UI and navigation to be tested before Oracle integration.

---

## CR-04: Repository Abstraction

Repositories should be abstracted where appropriate.

Example:

```scala
trait SupplierRepository {
  def findSuppliers(): Seq[Supplier]
}
```

Possible implementations:

```text
MockSupplierRepository
OracleSupplierRepository
```

---

## CR-05: Service Layer

Business operations shall be handled by services.

Example:

```scala
class DsdOrderService(
  supplierRepository: SupplierRepository,
  orderRepository: OrderRepository
)
```

---

## CR-06: Validation

Validation shall happen before executing database-changing operations.

---

## CR-07: Transaction Boundary

Transaction boundaries shall be clearly defined for operations involving multiple database updates.

---

## CR-08: Future Scope Protection

GitHub Copilot must not implement future modules unless explicitly requested.

---

## CR-09: Requirement Traceability

Each implementation must be traceable to a requirement or epic.

Example:

```text
EPIC-05
   ↓
FR-05.1
FR-05.2
FR-05.3
...
```

---

## CR-10: No Invented Business Rules

When the actual business rule is unknown, implementation shall use:

```text
TBC – To Be Confirmed
```

instead of inventing behavior.

---

# 9. Initial Data Strategy

Development shall use two phases.

## Phase 1 – Mock Data

```text
Wicket UI
   ↓
Service
   ↓
Mock Repository
```

### Purpose

- Build screens
- Verify navigation
- Verify filters
- Verify tables
- Verify user flow

---

## Phase 2 – Oracle Integration

```text
Wicket UI
   ↓
Service
   ↓
Oracle Repository
   ↓
JDBC
   ↓
Oracle
```

### Purpose

- Real database integration
- SQL validation
- Transaction testing
- PL/SQL integration
- Data consistency

---

# 10. Requirement Traceability

| Epic | Area | Main Requirements |
|---|---|---|
| EPIC-01 | Foundation | FR-01.1 – FR-01.4 |
| EPIC-02 | Login | FR-02.1 – FR-02.7 |
| EPIC-03 | Application Shell | FR-03.1 – FR-03.6 |
| EPIC-04 | Ordering Navigation | FR-04.1 – FR-04.4 |
| EPIC-05 | DSD Order | FR-05.1 – FR-05.9 |
| EPIC-06 | Stock Assignment | FR-06.1 – FR-06.7 |
| EPIC-07 | Steering Parameter | FR-07.1 – FR-07.7 |
| EPIC-08 | Database/PLSQL | FR-08.1 – FR-08.9 |
| EPIC-09 | Data Consistency | FR-09.1 – FR-09.7 |
| EPIC-10 | Testing | FR-10.1 – FR-10.6 |

---

# 11. Epic Dependency

The implementation order shall be:

```text
EPIC-01
   ↓
EPIC-02
   ↓
EPIC-03
   ↓
EPIC-04
   ↓
EPIC-05
   ↓
EPIC-06
   ↓
EPIC-07
   ↓
EPIC-08
   ↓
EPIC-09
   ↓
EPIC-10
```

---

# 12. Current Functional Flow

The primary application flow shall be:

```text
Login
  |
  v
Main Application
  |
  +-----------------------+
  |                       |
  v                       v
Ordering               Parameter
  |                       |
  +----------+            v
  |          |       Steering Parameter
  v          v
DSD Order   Stock Assignment
  |          |
  v          v
Filter      Filter
  |          |
  v          v
Results     Results
```

---

# 13. DSD Order Flow

```text
Ordering
   ↓
DSD Order
   ↓
Filter Criteria
   ↓
Select PUAR / SL / SID / Delivery Day
   ↓
Select Supplier
   ↓
Apply
   ↓
Retrieve Orders
   ↓
Display Order Result
```

---

# 14. Stock Assignment Flow

```text
Ordering
   ↓
Stock Assignment
   ↓
Filter Criteria
   ↓
Select DC / Assignment Day / Article Criteria
   ↓
Apply
   ↓
Retrieve Stock Information
   ↓
Display Assignment Result
```

---

# 15. Steering Parameter Flow

```text
Parameter
   ↓
Steering Parameter
   ↓
Load Current Settings
   ↓
Modify Settings
   ↓
Validate
   ↓
Save
   ↓
Commit
```

---

# 16. Database Flow

The final database flow shall be:

```text
Wicket Page
     ↓
Service
     ↓
Repository
     ↓
JDBC
     ↓
Oracle SQL / PL/SQL
     ↓
Oracle Tables
```

---

# 17. Data Consistency Flow

For a transactional operation:

```text
Request
   ↓
Input Validation
   ↓
Begin Transaction
   ↓
Read Required Data
   ↓
Validate Business Rules
   ↓
Perform Database Changes
   ↓
Validate Result
   ↓
COMMIT
   |
   +---- Failure ----> ROLLBACK
```

---

# 18. Development Rules for GitHub Copilot

GitHub Copilot shall follow these rules:

1. Implement only the currently requested epic.
2. Do not implement future modules.
3. Do not invent missing business rules.
4. Follow the project documentation.
5. Follow the layered architecture.
6. Keep Wicket pages free from direct SQL.
7. Use services for business operations.
8. Use repositories for database access.
9. Use parameterized JDBC queries.
10. Keep database transactions explicit.
11. Reuse existing components where possible.
12. Do not duplicate business logic unnecessarily.
13. Do not change unrelated files.
14. Do not introduce Maven when SBT is the project build system.
15. Do not replace Scala with Java unless explicitly requested.
16. Do not introduce additional frameworks without approval.
17. Update documentation when an approved requirement changes.

---

# 19. Definition of Done

A requirement shall be considered complete only when:

- The functionality is implemented.
- The UI is working.
- Validation is implemented where required.
- Error handling is implemented.
- The implementation follows the architecture.
- No unrelated scope has been added.
- Tests are added where applicable.
- Existing functionality continues to work.
- Documentation is updated if required.

---

# 20. Project Completion Criteria

The demo project shall be considered complete when:

1. Login works.
2. Logout works.
3. Common application shell works.
4. Ordering navigation works.
5. DSD Order works according to confirmed requirements.
6. Stock Assignment works according to confirmed requirements.
7. Steering Parameter works according to confirmed requirements.
8. Oracle integration works.
9. JDBC repositories work.
10. Required PL/SQL components work.
11. Transactions are correctly handled.
12. Data consistency is maintained.
13. Required tests pass.
14. The application can be built and run using SBT.

---

# 21. Requirement Change Control

Any new requirement shall be evaluated before implementation.

The change process shall be:

```text
New Requirement
      ↓
Requirement Review
      ↓
Scope Decision
      ↓
Documentation Update
      ↓
Epic Update
      ↓
Implementation
      ↓
Testing
```

GitHub Copilot must not treat a user suggestion or comment as an approved requirement until it is explicitly included in the project documentation.

---

# 22. Final Scope Summary

## Current Scope

```text
LOGIN
  ↓
APPLICATION SHELL
  ↓
+----------------------+
|                      |
ORDERING            PARAMETER
|                      |
+--------+             |
|        |             |
DSD      STOCK         |
ORDER    ASSIGNMENT    |
                       |
                STEERING PARAMETER
```

## Future Scope

```text
Additional Ordering Functions
Additional Parameter Functions
Additional Reports
Additional Interfaces
Additional Integrations
```

These future functions shall remain outside the current implementation unless explicitly approved.