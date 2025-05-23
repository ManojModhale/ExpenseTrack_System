# ExpenseTrack System Backend

## Overview

**ExpenseTrack** is a robust, secure, and feature-rich application developed using Spring Boot, aimed at simplifying expense tracking for employees and managers. This backend provides seamless integration with a MySQL database, secure authentication mechanisms, and RESTful APIs for efficient communication with the frontend.

As a Full Stack Developer, I took charge of all major roles, including frontend and backend development, ensuring a client-centric approach while maintaining a balance between design and functionality. The application follows the MVC design pattern and a layered architecture comprising controllers, services, repository, and models at the backend to ensure modularity, scalability, and maintainability.

## Features

1. **Multi-Role Functionality**:
    - Employee: Submit expenses, view approval status.
    - Manager: Review, approve, or reject expenses.
    - Admin: Manage users and system settings.

2. **Core Functionalities**:
    - User registration and login with secure password encryption (BCrypt).
    - Email notifications for expense status updates.
    - Expense receipt file upload and download.
    - API-driven architecture for seamless frontend-backend integration.

3. **Security**: 
    - REST API authentication with Spring Security.
    - Role-based access control (RBAC) for API endpoints.

4. **Scalable Design**: 
    - Layered architecture: Controllers, Services, Repositories, and Models.
    - Modular codebase for easy scalability and maintenance.

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

## Installation

### Prerequisites

Before starting, ensure you have the following installed:

- [Java JDK 11+](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/)
- [MySQL Server](https://dev.mysql.com/downloads/installer/)

**Technologies Used**:
  - Backend Framework: Spring Boot 3.3.11
  - Database: MySQL
  - ORM: Spring Data JPA
  - Security: Spring Security
  - Email: Spring Boot Mail
  - Build Tool: Maven


### Steps to Set Up the Backend

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/expense-management-backend.git
   cd expense-management-backend
   ```

2. Configure the database:
   - Create a MySQL database named `expense_management`.
   - Update the database connection details in `application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/expense_management
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     spring.jpa.hibernate.ddl-auto=update
     ```

3. Configure the email:
   - Update the email configuration details in `application.properties`:
     ```properties
     spring.mail.host=smtp.gmail.com
     spring.mail.port=465 or 587
     spring.mail.username=user email address
     spring.mail.password=user email password
     spring.mail.properties.mail.smtp.auth=true
     spring.mail.properties.mail.smtp.starttls.enable=true # Use TLS
     # If you're using SSL, you might need to set these properties instead of starttls:
     spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
     spring.mail.properties.mail.smtp.socketFactory.fallback=false
     spring.mail.properties.mail.smtp.socketFactory.port=465 or 587
     ```

4. Build the project:

   ```bash
   mvn clean install
   ```

5. Run the application:

   ```bash
   mvn spring-boot:run
   ```

6. Backend will be accessible at:

   ```
   http://localhost:8181
   ```

---

## Folder Structure

```
expense-management-backend/
|
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com\expensemanagement\backend/
│   │   │   │   ├── ExpenseManagementBackendApplication.java
│   │   │   │   ├── ServletInitializer.java
│   │   │   │   ├── config/               # Security 
│   │   │   │   │   ├── SecurityConfig.java 			# Security Related logic bcrypt password, cors and REST API Authentication
│   │   │   │   ├── controller/               # REST controllers for API endpoints
│   │   │   │   │   ├── UserController.java 			# For common user actions like registration and login
│   │   │   │   │   ├── EmployeeController.java 		# For Employee-specific actions
│   │   │   │   │   ├── ManagerController.java 		# For Manager-specific actions
│   │   │   │   │   ├── ExpenseController.java 		# For expense-related functionality
│   │   │   │   │   ├── AdminController.java 			# For Admin-specific actions
│   │   │   │   ├── service/                  # Business logic and services
│   │   │   │   │   ├── UserService.java 				# Shared logic for User authentication, registration
│   │   │   │   │   ├── EmailService.java 			# Email related all methods
│   │   │   │   │   ├── EmployeeService.java 			# Employee-specific business logic
│   │   │   │   │   ├── ManagerService.java 			# Manager-specific business logic
│   │   │   │   │   ├── ExpenseService.java 			# Expense-related logic
│   │   │   │   │   ├── FileStorageService.java 		# Expense Receipt file related logic
│   │   │   │   │   ├── AdminService.java 			# Admin-specific business logic
│   │   │   │   ├── repository/               # interfaces JPA repositories for database operations
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── ExpenseRepository.java
│   │   │   │   │   ├── ContactRepository.java
│   │   │   │   ├── dto/               		# DTO classes for returning as Response
│   │   │   │   │   ├── ExpenseDto.java
│   │   │   │   ├── exception/               	# custom exceptions
│   │   │   │   │   ├── FileStorageException.java
│   │   │   │   │   ├── MyFileNotFoundException.java
│   │   │   │   └── model/                    # Entity classes representing database tables
│   │   │   │   │   ├── Contact.java 					# For Contact form from frontend 
│   │   │   │   │   ├── User.java 					# Common for Employee and Manager
│   │   │   │   │   ├── Expense.java					# Expense
│   │   │   │   │   ├── Role.java 					# ENUM  for roles having (EMPLOYEE, MANAGER)
│   │   │   │   │   ├── Category.java 				# ENUM  for category having (FOOD, TRAVEL, LODGING, UTILITIES, OTHER)
│   │   │   │   │   ├── Status.java 					# ENUM  for status having (PENDING, APPROVED, REJECTED)
│   │   └── resources/
│   │       ├── application.properties         # Configuration properties
├── pom.xml                                    # Maven configuration
└── README.md                                  # Project documentation
```

---

## REST API Endpoints

### Authentication
- **POST /api/auth/register**: Register a new Employee or Manager.
- **POST /api/auth/login**: Authenticate a Employee or Manager and return a JWT.

### Employee
- **GET /api/employee/{id}**: Get employee details
- **POST /api/employee/addexpense**: Submit expense.
- **PUT /api/employee/update-expense**: Update a expense (existing expense). 
- **DELETE /api/employee/delete-expense/{productid}**: Delete a expense. 

### Managers
- **GET /api/managers/expenses**: managers/expenses: View all expenses.
- **PUT /api/managers/expense/{id}**: Approve or reject expense

### Admin
- **GET /api/admin/users**: Manage users
- **PUT /api/managers/expense/{id}**: Approve or reject expense

---

## Contact

Maintainer: Manoj Modhale

- GitHub: [https://github.com/ManojModhale](https://github.com/ManojModhale)
- Email: [manojmodhale2001@gmail.com](mailto:manojmodhale2001@gmail.com)

