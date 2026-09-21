AI Based Resume Builder

A Spring Boot backend project for building and managing resumes with
secure authentication, role-based authorization, user management, API
logging, and resume management...

🚀 Project Overview

The goal of this project is to build a secure and scalable Resume
Builder application where users can create and manage multiple resumes.
The project is being developed step by step with a production-oriented
backend architecture...

🛠️ Technologies Used ...!

Java 17

Spring Boot

Spring Web

Spring Data JPA

Hibernate

MySQL

Spring Security

JWT

BCrypt

Maven

Bean Validation

Git & GitHub

📁 Project Structure

src/main/java/com/switchproject/demo
│
├── controller
├── dto
├── exception
├── model
├── repository
├── security
└── service

The project follows a layered architecture:

Client / Postman
↓
Controller
↓
Service
↓
Repository
↓
MySQL

🔐 Authentication & Security

JWT Authentication

The application uses JWT-based stateless authentication.

Login
↓
Validate email & password
↓
Generate JWT
↓
Return token
↓
Send token with protected requests

Protected requests use:

Authorization: Bearer <JWT_TOKEN>

Password Security

Passwords are stored using BCrypt hashing rather than plain text.

Role-Based Authorization

The application currently supports:

USER

ADMIN

Example authorization:

/admin/**       → ADMIN only
/users/all      → ADMIN only
/user/**        → USER or ADMIN
/login          → Public
/register       → Public

A normal user cannot access admin-only endpoints.

👤 User Management

Implemented user functionality includes:

User registration

User login

Get user by ID

Get all users

Update user

Delete user

New users are assigned the USER role by default during registration.

✅ Validation

Bean Validation is used for validating incoming requests.

Example:

@NotBlank
@Size(min = 8, max = 20)
private String password;

Controllers use @Valid to trigger validation.

⚠️ Global Exception Handling...!

The application uses @ControllerAdvice for centralized exception
handling.

Custom exceptions include:

1) UserNotFoundException

2) InvalidCredentialException

Validation errors are also returned through a common ErrorResponse
structure.

📊 API Logging

Every API request is logged into the MySQL api_logs table.

1) The logger captures:

Full URL

HTTP method

User email

Request body

Response body

HTTP status code

Timestamp

Response time in milliseconds

The logging filter uses:

OncePerRequestFilter

ContentCachingRequestWrapper

ContentCachingResponseWrapper

Sensitive information is masked before being stored:

password → ********
token    → [HIDDEN]

👨‍💼 Admin Dashboard API

An admin dashboard API has been added:

GET /admin/dashboard

It provides summary information such as:

{
"totalUsers": 10,
"totalAdmins": 1,
"totalNormalUsers": 9,
"totalApiHits": 150
}

The endpoint is protected and accessible only to users with the ADMIN
role.

📄 Resume Management

Resume management is currently under development.

Current functionality

Create a resume:

POST /resumes

Request:

{
"title": "Java Backend Developer Resume"
}

The logged-in user is identified from the JWT authentication instead of
accepting a userId from the client.

Get the logged-in user's resumes:

GET /resumes

The API returns only resumes belonging to the authenticated user.

Resume Relationship

A user can have multiple resumes:

User
├── Resume 1
├── Resume 2
└── Resume 3

This is implemented using a JPA @ManyToOne relationship from Resume
to User.

▶️ Running the Project

1. Clone the repository

git clone git@github.com:RaviWaghachavare/AiBasedResumeBuilder.git

2. Configure MySQL

Update the database configuration in:

src/main/resources/application.properties

Configure your:

Database URL

Username

Password

3. Run the application

Using Maven:

mvn spring-boot:run

Or run the main Spring Boot application from IntelliJ IDEA.

🧪 API Testing

Postman is currently used for API testing.

Example:

POST http://localhost:8080/login
POST http://localhost:8080/register
GET  http://localhost:8080/users/all
GET  http://localhost:8080/admin/dashboard
POST http://localhost:8080/resumes
GET  http://localhost:8080/resumes

Protected APIs require a valid JWT token.

📌 Current Development Status

Completed

User Registration

User Login

BCrypt Password Hashing

JWT Authentication

JWT Filter

Role-Based Authorization

User CRUD

DTO Pattern

Request Validation

Global Exception Handling

API Logging

Sensitive Data Masking

Admin Dashboard API

Resume Entity

Resume Repository

Create Resume API

Get Current User's Resumes API

🔜 Planned

Resume Response DTO

Get Resume by ID

Update Resume

Delete Resume

Resume personal information

Education section

Experience section

Skills section

Projects section

Certifications

Resume preview

PDF generation

Resume download

Refresh Token

Swagger / OpenAPI documentation

AI Resume Improvement

ATS Score

Job Description Matching

Angular Frontend

Admin Dashboard UI

Docker

Deployment

🎯 Long-Term Goal

Build a complete AI-powered Resume Builder that allows users to:

Create multiple resumes

Manage resume sections

Generate professional resumes

Download resumes as PDF.

Improve resumes using AI.

Check ATS compatibility.

Match resumes against job descriptions

The project is being developed incrementally with focus on clean
architecture, security, maintainability, and real-world backend
practices.