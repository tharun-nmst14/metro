# Metro UFM Demo - Project Overview

**Document:** 01-project-overview.md  
**Version:** 1.0  
**Status:** Baseline  
**Project:** Metro UFM Demo

---

## 1. Project Purpose

The Metro UFM Demo is an enterprise-style demonstration application inspired
by the reference screens of a Metro Ultra Fresh Management (UFM) application.

The purpose of this project is to reproduce the relevant application flow,
navigation, user interface structure, filtering screens, data tables, and
configuration screens using Scala and Apache Wicket.

The project is also intended as a practical learning project for:

- Scala
- SBT
- Apache Wicket
- Enterprise web application architecture
- JDBC
- Oracle Database
- PL/SQL
- Transaction management
- Data consistency
- Automated testing

This project is a standalone demonstration application.

It must not be treated as the production Metro UFM application and must not
assume access to production systems, production databases, credentials, or
production business logic.

---

# 2. Project Objectives

The project has the following objectives.

### 2.1 Application Development

Build a working web application using:

- Scala
- SBT
- Apache Wicket
- Jetty
- HTML
- CSS
- JDBC
- Oracle Database
- PL/SQL

### 2.2 UI Replication

Reproduce the structure and general appearance of the provided reference
screens.

The implementation should focus on:

- Page layout
- Navigation
- Forms
- Filter dialogs
- Tables
- Buttons
- Input controls
- Tabs
- Context information
- User interaction

Visual details should be refined progressively.

### 2.3 Enterprise Architecture

The application should use a clear separation between:

```text
Presentation
     ↓
Service
     ↓
Repository
     ↓
JDBC
     ↓
Oracle / PL/SQL