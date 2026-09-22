# Online Project Management Tool

## The JVM Crew

A web-based Online Project Management Tool developed using Java, Jakarta Servlets, JSP, JDBC, MySQL, HTML, CSS, and JavaScript.

The system helps project managers create projects, manage team members, create and assign tasks, monitor task progress, and maintain an activity history. Administrators manage users and roles, while team members track and update their assigned tasks.

---

# Team Members

| Name | Role |
|---|---|
| Raj Katyayan Gautam | Team Leader / Project Manager |
| Nagesh Mishra | Team Member |
| Tiya Chauhan | Team Member |
| Aman Yadav | Team Member |

### Team Name
**The JVM Crew**

---

# Project Objective

The objective of this project is to provide a centralized platform for managing projects, teams, tasks, assignments, and progress.

The system reduces the need for scattered task tracking and provides role-based dashboards for administrators, project managers, and team members.

---

# User Roles

## 1. Administrator

The administrator manages:

- User accounts
- User roles
- System activity
- Role-based access

### Admin Dashboard

- User Management
- Role Management
- Recent Activity Feed
- System-level monitoring

---

## 2. Project Manager

The project manager manages:

- Projects
- Team members
- Tasks
- Task assignments
- Project progress

### Manager Dashboard

- Project statistics
- Project list
- Project details
- Team management
- Task management

---

## 3. Team Member

Team members can:

- View assigned tasks
- Track tasks
- Update task status
- View relevant project information

### Member Dashboard

- My Tasks
- Task status updates
- Assigned project work

---

# Main Features

## Authentication

- User registration
- User login
- Logout
- Session management
- Password hashing
- Duplicate email detection
- Input validation

## Project Management

- Create project
- View project
- Delete project
- Project status
- Project priority
- Project dates

## Team Management

- Add members
- View team members
- Kick members from project

## Task Management

- Create tasks
- Assign tasks
- Unassign tasks
- Update task status
- Delete tasks

## Activity Logging

The application records important actions such as:

- Project creation
- Project deletion
- Member addition
- Member removal
- Task creation
- Task assignment
- Task unassignment
- Task deletion
- Task status updates
- Role changes

## Dashboard Statistics

Manager statistics include:

- Total Projects
- Total Tasks
- In Progress Tasks
- Completed Tasks
- Team Members

---

# Security Features

The application includes multiple security protections:

### Authentication

Only authenticated users can access protected dashboards.

### Role-Based Authorization

Different application areas are restricted according to:

- ADMIN
- MANAGER
- MEMBER

### Password Security

Passwords are stored using password hashing rather than plain text.

### SQL Injection Protection

Database operations use `PreparedStatement`.

### XSS Protection

User-generated content is HTML-escaped before being rendered in JSP pages.

### CSRF Protection

State-changing requests are protected through origin validation.

### Session Security

- Session renewal after successful login
- Logout session invalidation
- Session timeout
- Protected-page cache prevention

### HTTP Security Headers

The application provides browser security headers including:

- X-Content-Type-Options
- X-Frame-Options
- Content-Security-Policy
- Referrer-Policy
- Permissions-Policy
- HTTPS HSTS support

---

# Technology Stack

## Frontend

- HTML5
- CSS3
- JavaScript
- JSP

## Backend

- Java 26
- Jakarta Servlets
- Jakarta JSP
- Jakarta JSTL

## Database

- MySQL
- JDBC

## Server

- Apache Tomcat 10.1.60

## Build Tool

- Apache Maven

## Development Tools

- VS Code / Notepad++
- Git
- GitHub

---

# System Architecture

```text
                    Browser
                       |
                       v
              JSP / HTML / CSS / JS
                       |
                       v
                 Jakarta Servlets
                       |
                       v
                    Service
                       |
                       v
                      DAO
                       |
                       v
                     JDBC
                       |
                       v
                    MySQL
					
					

```

# Project Structure

```text
OnlineProjectManagementTool/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── jvmcrew/
│       │           ├── model/
│       │           ├── dao/
│       │           ├── service/
│       │           ├── controller/
│       │           ├── api/
│       │           ├── filter/
│       │           ├── util/
│       │           └── exception/
│       │
│       └── webapp/
│           ├── admin/
│           ├── manager/
│           ├── member/
│           ├── css/
│           ├── js/
│           ├── WEB-INF/
│           ├── index.jsp
│           ├── login.jsp
│           └── register.jsp
│
├── pom.xml
├── deploy.bat
└── README.md
```

# Database Design

The application uses the following main tables:

1. users: Stores user account and role information.

2. projects: Stores project information and project manager ownership.

3. project_members: Maintains the relationship between projects and team members.

4. tasks: Stores project tasks, priorities, statuses, and deadlines.

5. task_assignments: Stores task-to-member assignments.

6. activity_logs: Stores important system actions for audit history.



# Application Flow:

## Registration
User
  ↓
Register Form
  ↓
RegisterServlet
  ↓
Validation
  ↓
Password Hashing
  ↓
UserDAO
  ↓
MySQL

## Login
User
  ↓
Login Form
  ↓
LoginServlet
  ↓
UserDAO
  ↓
Password Verification
  ↓
Session Creation
  ↓
Role-Based Dashboard

## Task Management
Manager
  ↓
Create Task
  ↓
CreateTaskServlet
  ↓
TaskDAO
  ↓
MySQL
  ↓
Assign Task
  ↓
Team Member
  ↓
Update Status

# Running the Project

## 1. Start MySQL

Make sure MySQL Server is running.

## 2. Start Tomcat
cd /d "C:\Coding & Skills\apache-tomcat-10.1.60\bin"
startup.bat

## 3. Build the project
cd /d "C:\Coding & Skills\OnlineProjectManagementTool"
mvn clean package

## 4. Deploy
deploy.bat

## 5. Verify the server
```cmd
timeout /t 3
netstat -ano | findstr :8080
curl -I --max-time 5 http://localhost:8080/
curl -I --max-time 5 http://localhost:8080/project-management-tool/
```

## 6. Open the application
http://localhost:8080/project-management-tool/


# Testing
The application has been tested for:

Environment and homepage
Registration
Login
Session handling
Manager dashboard
Project creation
Team member management
Task creation
Task assignment
Task status updates
Admin dashboard
Admin role management
Logout
Access control
Database persistence
Responsive UI
SQL injection protection
Stored XSS protection
CSRF protection
Session security
Security headers
Task deletion
Project deletion

# Future Improvements

Possible future enhancements include:

Project analytics
Progress charts
Notifications
Comments
Search and filtering
User profile management
Advanced reporting
Email notifications
File attachments
Project deadlines and reminders


# Conclusion

The Online Project Management Tool provides a centralized environment for managing projects, teams, tasks, assignments, and progress.

The application demonstrates the use of Java programming, object-oriented design, Jakarta Servlets, JSP, JDBC, MySQL, authentication, authorization, database operations, and web security concepts in a practical project.

# Team: The JVM Crew

Raj Katyayan Gautam
Team Leader / Project Manager

Nagesh Mishra
Team Member

Tiya Chauhan
Team Member

Aman Yadav
Team Member
