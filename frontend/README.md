# ExpenseTrack System Frontend

## Overview

**ExpenseTrack System Frontend** is a React.js-based application designed to streamline expense management for employees and managers. The platform is fully responsive, user-friendly, and integrates seamlessly with the backend REST API to deliver a smooth and efficient user experience.

This project incorporates modern technologies like React-Bootstrap, Material UI for styling, Recharts for expense reporting, and sessionStorage for session management. The system supports role-based functionality with distinct interfaces and features for employees and managers.

## Features

1. **Multi-Role Functionality**:
    - Employee: Submit, view, edit, and delete personal expenses.
    - Manager: View and manage expenses submitted by employees, including approval and rejection.
    - Admin: Oversee system operations, manage users, and view reports.

2. **Core Functionalities**:
    - User registration and login with secure authentication and password encryption (BCrypt).
    - Email notifications for expense status updates.
    - Expense receipt file upload and download.
    - API-driven architecture for seamless frontend-backend integration.

3. **Highlights**:
    - Modular, layered architecture ensuring scalability and maintainability.
    - Support for file uploads for receipt attachments.
    - Real-time notifications powered by email services.
    - Enum-based roles and categories for type safety and consistency.
      
4. **Security**: 
    - REST API authentication with Spring Security.
    - Role-based access control (RBAC) for API endpoints.

5. **Scalable Design**: 
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

- [Node.js](https://nodejs.org/) (which includes npm)

Additionally, ensure the backend (Spring Boot) is running on port `8181`.

## Technologies Used

### Frontend
- **React.js**: Component-based framework for building the user interface.
- **React-Bootstrap**: Used for modal functionality and responsive components.
- **Material UI**: Provides additional styling and iconography.
- **Recharts**: Generates dynamic and visually appealing expense reports.
- **CSS**: Custom styling for components and pages.
- **sessionStorage**: For managing session data.

### Database
- **MySQL**: Relational database for storing application data.

### Tools
- **VS Code**: IDE for frontend development.
- **Any Browser**: UI testing and debugging.
- **Git**: Version control.


### Steps to Run the ReactJs Frontend

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/expensetrack-frontend.git
   cd expensetrack-frontend
   ```

2. Install the dependencies:

   ```bash
   npm install
   ```

3. Start the development server:

   ```bash
   npm start
   ```

4. Open your browser and navigate to:

   ```
   http://localhost:3000
   ```

The ReactJs frontend will be running and communicating with the backend on port `8181`.

---

## Folder Structure

```
├── expense-management-frontend/     # Frontend Code
│   ├── public/                	  # Static files
│   │   ├── index.html              	  # Main HTML file
│   │   └── assets/                 	  # Static assets (images, icons, etc.)
│   ├── src/                   	  # React app source
│   │   ├── components/        	  # Reusable components
│   │   │   ├── userNavbar.jxs  			# dynamic navbar for employee & manager    
│   │   │   ├── userNavbar.css          
│   │   │   ├── Loader.jsx          		# Loading spinner
│   │   │   ├── Loader.css
│   │   │   └── Notification.jsx    		# Toast/alert component
│   │   ├── pages/                  # Page-specific components
│   │   │   ├── LoginPage.jsx       		# Login functionality
│   │   │   ├── LoginPage.css
│   │   │   ├── RegisterPage.jsx    		# User registration
│   │   │   ├── RegisterPage.css
│   │   │   ├── LandingPage.jsx     		# Landing page
│   │   │   ├── LandingPage.css
│   │   │   ├── ForgotPasswordPage.jsx    	# Forgot Password and Reset New Password
│   │   │   ├── ForgotPasswordPage.css
│   │   │   ├── UserProfile.jsx    		# Dynamic Profile page for Employee & Manager
│   │   │   ├── UserProfile.css
│   │   │   ├── ExpenseReportModal.jsx    # Dynamic Expense Report Modal Page
│   │   │   ├── NotFoundPage.jsx    		# Not found page
│   │   │   ├── Employee/
│   │   │   │   ├── EmployeeDashboard.jsx 	# Employee dashboard
│   │   │   │   ├── EmployeeDashboard.css
│   │   │   │   ├── MyExpenseEmp.jsx 			# List of all expenses for logged in employee can be edit, delete or add new expense.
│   │   │   │   ├── MyExpenseEmp.css
│   │   │   ├── Manager/
│   │   │   │   ├── AllEmployeeExpenses.jsx   # Expenses List of All Employees for Approve/reject expense
│   │   │   │   ├── AllEmployeeExpenses.css   
│   │   │   │   ├── ManagerDashboard.jsx 		# Manager dashboard
│   │   │   │   ├── ManagerDashboard.css
│   │   │   │   ├── MyExpenseMg.jsx 			# List of all expenses for logged in manager can be edit, delete or add new expense.
│   │   │   │   ├── MyExpenseMg.css   
│   │   ├── services/        		# API interaction (Axios calls)
│   │   │   ├── authService.js     		# Authentication (login, register) 
│   │   │   ├── expenseService.js  		# Expense-related API calls
│   │   │   └── userService.js    			# User-related API calls 
│   │   ├── app.js              	# Main App component
│   │   ├── app.css            	# Main App styles
│   │   ├── index.js           	# React DOM entry point
│   │   ├── index.css          	# Global styles
│   ├── .env                   	# Environment variables
│   ├── package-lock.json      
│   ├── package.json           
│   ├── gitignore              
│   └── README.md              
```

---

## Folder Description

- **public/assets/**: Contains static files such as images and configuration JSON files.
- **src/components/**: Contains reusable components.
- **src/services/**: Houses services that handle business logic and backend communication.
- **src/pages/**: Contains Common components or pages for user
- **src/pages/Employee/**: Contains Employee realed Components & UI files.
- **src/pages/Manager/**: Contains Manager realed Components & UI files.

---

## Backend Communication

The ReactJs frontend communicates with the Spring Boot backend running on port `8181` using React's `Axios`. All HTTP requests (GET, POST, PUT, DELETE) for user authentication, user data, expense processing, and expense handling are directed to the backend URL `http://localhost:8181`.

**Example API Endpoints**

- `GET | POST | PUT /api/users`: User Management data.
- `GET | POST | PUT | DELETE /api/employee`: Employee Operations.
- `GET | POST | PUT | DELETE /api/manager`: Manager Operations.

**CORS Configuration (Backend)**

Ensure that CORS is enabled in the Spring Boot backend to allow communication between the frontend (port `3000`) and backend (port `8181`).

---

## Future Enhancements

- Add email notifications for approved/rejected expenses.
- Implement JWT-based authentication for enhanced security.
- Support for uploading and managing receipts as file attachments.
- Multi-language support for wider accessibility.

---

## Contact

Maintainer: Manoj Modhale

- GitHub: [https://github.com/ManojModhale](https://github.com/ManojModhale)
- Email: [manojmodhale2001@gmail.com](mailto:manojmodhale2001@gmail.com)

---
