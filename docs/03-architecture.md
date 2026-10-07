# Metro UFM Demo – Architecture Specification

## 1. Document Information

| Item | Details |
|---|---|
| Project | Metro UFM Demo |
| Document | Architecture Specification |
| Version | 1.0 |
| Status | Draft / Development Baseline |
| Technology | Scala + SBT + Apache Wicket + Jetty + Oracle |
| Database Access | JDBC |
| Database Logic | SQL / PL/SQL |
| UI | Apache Wicket + HTML + CSS |
| Build Tool | SBT |

---

# 2. Purpose

This document defines the technical architecture for the Metro UFM Demo application.

The architecture is designed to:

- Keep UI, business logic, and database logic separated.
- Make the application easy to understand and maintain.
- Support incremental development through GitHub Copilot.
- Allow UI development using mock/in-memory data before Oracle integration.
- Support Oracle JDBC and PL/SQL integration later.
- Provide a clear structure for enterprise-style Scala development.
- Maintain data consistency for business operations.
- Avoid putting business logic or database code directly inside Wicket pages.

---

# 3. Architecture Goals

The application architecture must satisfy the following goals:

1. Clear separation of responsibilities.
2. Reusable Wicket components.
3. Business logic centralized in service classes.
4. Database operations centralized in repository classes.
5. No direct JDBC code inside Wicket pages.
6. Parameterized SQL for database operations.
7. Oracle transactions for multi-step operations.
8. Ability to replace mock repositories with Oracle repositories.
9. Easy unit testing of services.
10. Easy future expansion of the application.

---

# 4. High-Level Architecture

The application follows a layered architecture.

```text
+--------------------------------------------------+
|                    Browser                       |
|              HTML / CSS / HTTP                   |
+-------------------------+------------------------+
                          |
                          v
+--------------------------------------------------+
|              Apache Wicket UI Layer              |
|                                                  |
|  Pages                                           |
|  Panels                                          |
|  Forms                                           |
|  Tables                                          |
|  Filters                                         |
+-------------------------+------------------------+
                          |
                          v
+--------------------------------------------------+
|                Service Layer                     |
|                                                  |
|  Login Service                                   |
|  DSD Order Service                               |
|  Stock Assignment Service                        |
|  Steering Parameter Service                      |
+-------------------------+------------------------+
                          |
                          v
+--------------------------------------------------+
|              Repository Layer                    |
|                                                  |
|  Login Repository                                |
|  Supplier Repository                             |
|  DSD Order Repository                            |
|  Stock Assignment Repository                     |
|  Steering Parameter Repository                   |
+-------------------------+------------------------+
                          |
                          v
+--------------------------------------------------+
|             Database Access Layer                |
|                                                  |
|  JDBC                                            |
|  Connection Management                           |
|  Transactions                                    |
|  SQL / Callable Statements                       |
+-------------------------+------------------------+
                          |
                          v
+--------------------------------------------------+
|                  Oracle DB                       |
|                                                  |
|  Tables                                          |
|  Views                                           |
|  Constraints                                     |
|  PL/SQL Packages                                 |
|  Procedures                                      |
|  Functions                                       |
+--------------------------------------------------+