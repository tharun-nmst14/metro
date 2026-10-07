# GitHub Copilot Instructions

## Project Rules

- This project uses Scala, SBT, Apache Wicket, Jetty, Oracle Database, PL/SQL, JDBC, HTML, and CSS.
- Keep package naming under `com.metro.ufm`.
- Do not introduce Maven, Spring Boot, Play Framework, Akka HTTP, or another web framework.
- Do not implement application functionality until the corresponding epic is explicitly started.
- Keep Wicket pages and panels focused on presentation concerns.
- Keep database access out of Wicket pages.
- Route database access through an application/service layer and repository layer using JDBC.
- Use parameterized SQL when SQL is introduced.
- Never concatenate user input into SQL.
- Do not create database schemas, PL/SQL scripts, mock data, services, repositories, or business logic during scaffold-only work.
- Preserve the documented current scope and out-of-scope boundaries unless the user explicitly changes them.
