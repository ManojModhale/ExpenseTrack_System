# ExpenseTrack_System

## Overview

**ExpenseTrack_System** is a robust and scalable expense management platform developed using ReactJS, Spring Boot, MySQL, and Recharts. Designed to streamline expense tracking and approval processes, it supports multi-role functionality for employees, managers, and administrators, each with unique access levels and features.

This project follows a layered architecture and MVC design pattern to ensure modularity, scalability, and maintainability. The intuitive user interface and insightful analytics enhance usability, making it ideal for enterprise-level expense management.

## Features

1. **Multi-Role Functionality**:
    - Employee: 
        - Register, log in, and access a personalized dashboard.
        - View rejected, approved, and pending expenses counts.
        - Manage personal expenses with options to add, edit, or delete expenses.
        - Generate expense reports with pie and bar charts based on criteria like monthly, weekly, or category-wise distributions.
    - Manager:
        - Register, log in, and access a personalized dashboard.
        - View rejected, approved, and pending expenses counts.
        - Manage personal expenses with options to add, edit, or delete expenses.
        - Generate expense reports with pie and bar charts based on criteria like monthly, weekly, or category-wise distributions.
        - Additional functionality to view, approve, or reject expenses submitted by employees.
    - Admin:
        - Add or delete employees and managers.
        - Manage and review all expenses, including approval or rejection for employees and managers.

2. **Core Functionalities**:
    - User Authentication: Secure account creation and login for all roles.
    - Dashboard Analytics:
        - Insights into expense counts and trends using category-wise pie charts.
        - Recent expense tracking with clear status indicators.
    - Expense Management:
        - Add, edit, delete, and track expenses efficiently.
        - Category-based filtering and reporting for better expense insights.
    - Report Generation:
        - Interactive charts for expense analysis, including pie charts and bar graphs.
        - Reports generated based on time periods and categories.
    - Approval Workflow:
        - Managers and admins can approve or reject expenses with role-based access controls.

3. **Project Highlights**: 
    - Scalable Architecture: Follows the MVC design pattern with a layered architecture for modularity and maintainability.
    - Intuitive User Interface: Built with Material UI and Recharts for a clean and responsive experience.
    - Analytics Integration: Leverages Recharts for dynamic and interactive data visualizations.
    - Role-Based Features: Distinct functionalities tailored to the needs of employees, managers, and admins.
    - Comprehensive Expense Management: End-to-end solutions for expense creation, approval, and analysis.
    - Responsive Design: Optimized for various devices to ensure a consistent user experience.

---

## Frontend (ReactJS)

The frontend of ExpenseTrack_System is built using ReactJS, ensuring a seamless and responsive user experience. The interface is designed for clarity, accessibility, and ease of use across various devices.

## Key Features

1. **Components**:
    - Modular and reusable components like Dashboard, MyExpenses, ExpenseReport, and AdminPanel.
    - State management using React’s Context API for efficient data handling and sharing.

2. **Routing**:
    - React Router for navigation between pages.
    - Protected routes to ensure secure access based on roles.
    
3. **Charts and Analytics**:
    - Recharts library for generating interactive and insightful expense reports.
    
4. **UI and Responsiveness**:
    - Styled using Material UI and React Bootstrap for a clean, professional look.
    - Media queries for optimal viewing on different devices.
      
5. **HTTP Requests**:
    - Axios for seamless interaction with backend APIs.

**Technologies and Tools**:
  - ReactJS
  - Recharts for analytics
  - Axios for HTTP requests
  - React Bootstrap/Material UI Design for styling, responsiveness and layout
  - VS Code
  - Git and Github

---

## Backend (Spring Boot)

The backend of ExpenseTrack_System is developed using Spring Boot, providing a robust foundation for secure data handling, business logic execution, and seamless API integration.

## Architecture and Flow:

1. **Request Flow**:
    - The frontend sends HTTP requests to the controller layer.
    - The service layer implements core business logic.
    - The repository layer interacts with the database using JPA for data persistence.
    - DTO Class Objects are returned as Responses.
    - Responses are returned to the frontend via REST APIs.

2. **Role-Based Access Control**:
    - Implements role-specific functionalities and data visibility.

3. **Expense Management**:
    - Handles CRUD operations for expenses.
    - Processes approval or rejection of expenses.
    
4. **Database Integration**:
    - MySQL database with Spring Data JPA for efficient ORM.
    - Category and role fields are managed using enums.


**Key Features**:
  - Layered Architecture: Ensures separation of concerns between controllers, services, Repository, Model and DTOs.
  - Annotations: Used extensively for configuration and dependency injection.
  - Pom.xml: Manages dependencies and project configurations.


**Technologies and Tools**:
  - Spring Boot
  - Java
  - JPA for ORM
  - Maven
  - SpringToolSuite
  - Git and Github

---

## Database (MySQL)

1. **Schema**:
    - Tables for users, roles, expenses, and approvals.
    - Relationships:
        - One-to-Many: Users and Expenses.
        - Many-to-One: Expenses and Categories.

2. **Stored Data**:
    - User details (employees, managers, admins).
    - Expense records with status (approved, rejected, pending).
    - Logs for expense updates and actions.

---
