# AI Based Resume Builder

AI Based Resume Builder is a Spring Boot backend project for building and managing resumes with secure user authentication and role-based authorization.

## 🚀 Features


- User Registration
- User Login
- JWT Authentication
- BCrypt Password Encryption
- Role-Based Authorization
- User CRUD Operations
- DTO Pattern
- Request Validation
- Global Exception Handling
- API Request/Response Logging
- MySQL Database
- Pagination

## 🛠️ Technologies

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- MySQL
- Maven

## 🔐 Security

The application uses JWT-based authentication.

Roles:

- `USER`
- `ADMIN`

Admin-only APIs are protected using role-based authorization.

## 📊 API Logging

The application stores API activity in the `api_logs` table.

Logged information includes:

- Full API URL
- HTTP Method
- User Email
- Request Body
- Response Body
- Status Code
- Response Time
- Timestamp

Sensitive information such as passwords and JWT tokens is masked before being stored.

## ▶️ How to Run

### 1. Clone the repository


```bash
git clone git@github.com:RaviWaghachavare/AiBasedResumeBuilder.git
```

### CMD
git clone git@github.com:RaviWaghachavare/AiBasedResumeBuilder.git


