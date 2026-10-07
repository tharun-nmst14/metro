# Metro UFM Demo – Application Flow Specification

## 1. Document Information

| Item | Details |
|---|---|
| Project | Metro UFM Demo |
| Document | Application Flow Specification |
| Version | 1.0 |
| Status | Draft / Development Baseline |

---

# 2. Purpose

This document defines the end-to-end application flow of the Metro UFM Demo.

The document describes:

- Login flow
- Common application shell
- Navigation
- Ordering module flow
- DSD Order flow
- Stock Assignment flow
- Parameter module flow
- Steering Parameter flow
- Error and validation flow
- General navigation behavior

---

# 3. Overall Application Flow

```text
                    START
                      |
                      v
                +-----------+
                | Login Page |
                +-----------+
                      |
                 Login Valid?
                 /          \
               No            Yes
               |              |
               v              v
        Error Message     Main Application
                              |
              +---------------+---------------+
              |                               |
              v                               v
          Ordering                         Parameter
              |                               |
        +-----+------+                        |
        |            |                        v
        v            v               Steering Parameter
   DSD Order   Stock Assignment
        |            |
        v            v
      Result       Result