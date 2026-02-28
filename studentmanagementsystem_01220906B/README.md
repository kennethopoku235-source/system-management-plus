Student Management System (SMS Plus)
Developer: 01221722b

Submission Date: February 23, 2026

1. Project Overview
   A robust JavaFX-based management system designed for academic administrators to track student performance, manage enrollments, and generate automated reports.

2. Core Features
   Secure Authentication: Admin-only access with encrypted-style credential checking.

    Advanced CRUD: Full Create, Read, Update, and Delete capabilities.

    Temporal Tracking: Automatically records the Date Added for every student record.

    Data Portability:

    Automated Export: Generates CSV reports (All, Top Performers, At-Risk) saved directly to the /data project folder.

    Smart Import: Bulk student registration via CSV with built-in duplicate rejection.

3. Data Validation Rules
   The system enforces strict data integrity before any record enters the database:
   | Field | Validation Rule |
   | :--- | :--- |
   | Student ID | Must be unique and alphanumeric (no symbols/spaces). |
   | Full Name | Must be 2-60 characters; letters and spaces only. |
   | Email | Must follow standard name@domain.com syntax. |
   | GPA | Must be a numeric value between 0.0 and 4.0. |
   | Phone | Must be a numeric string of 10-15 digits. |

4. Import Logic & Error Handling
   The Import system is designed to be "crash-proof":

    Validation: Every row in a CSV is checked against the rules above.

    Duplicate Rejection: If a Student ID already exists in the database, the row is skipped.

    Error Reporting: Instead of crashing on bad data, the system generates a real-time Import Summary Report listing exactly which rows failed and why.

5. System Requirements
   Java: JDK 17 or higher

    JavaFX: Version 17.0.2+

    Database: SQLite (Automated table generation)

    Storage: A /data folder must exist in the root directory for exports.