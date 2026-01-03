# Task Management System

A comprehensive task management system built with Spring Boot 3.4.12, featuring user authentication, task management, and real-time capabilities using PostgreSQL and Redis.

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [Contributing](#contributing)

## 🎯 Overview

The Task Management System is a full-featured REST API built with a modular monolith architecture that allows users to:
- **User Authentication**: Register, login, and JWT-based authentication with refresh tokens
- **Project Management**: Create, read, update, delete, and search projects with pagination
- **Team Collaboration**: Manage project members and team collaboration
- **Security**: Role-based access control (USER, ADMIN) with Spring Security
- **Data Integrity**: Soft deletes for all entities with automatic timestamp management

## 🛠 Tech Stack

### Backend
- **Framework**: Spring Boot 3.4.12
- **Java Version**: 17
- **Build Tool**: Maven 3.x
- **Architecture**: Modular Monolith (Multi-module)

### Database & Cache
- **Database**: PostgreSQL 16.11 (Alpine 3.23)
- **Cache**: Redis (Latest)
- **Migrations**: Flyway Database Migration

### Key Libraries & Dependencies
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security with JWT (JJWT 0.13.0)
- **API Documentation**: SpringDoc OpenAPI 2.8.5 (Swagger UI)
- **Validation**: Spring Validation (Jakarta Validation)
- **Object Mapping**: MapStruct 1.6.3
- **Monitoring**: Spring Boot Actuator
- **Code Simplification**: Project Lombok
- **Database Driver**: PostgreSQL JDBC Driver

### Testing
- **Unit Testing**: JUnit 5 (Jupiter), Mockito
- **Integration Testing**: Spring Boot Test, Testcontainers 1.20.6
- **Security Testing**: Spring Security Test
- **Assertions**: AssertJ

### Development Tools
- **Hot Reload**: Spring Boot DevTools
- **AOP**: Spring AOP (for logging and cross-cutting concerns)
- **Docker**: Docker Compose for local development environment

## 📁 Project Structure

The project follows a **Modular Monolith Architecture** with clear separation of concerns:

```
task-management-system/
├── module-app/                          # Application Entry Point Module
│   ├── src/main/
│   │   ├── java/.../app/
│   │   │   ├── TaskManagementSystemApplication.java
│   │   │   └── config/               # Application-level configuration
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-local.properties
│   │       ├── banner.txt
│   │       └── db/migration/          # Flyway database migrations
│   │           ├── V1__initial_database.sql
│   │           ├── V2__create_users_table.sql
│   │           ├── V3__create_refresh_token_table.sql
│   │           ├── V4__create_projects_table.sql
│   │           └── V5__create_project_members_table.sql
│   └── pom.xml
│
├── module-core/                         # Core/Shared Module
│   ├── src/main/java/.../core/
│   │   ├── annotation/               # Custom annotations (@LogExecutionTime)
│   │   ├── aspect/                   # AOP aspects for cross-cutting concerns
│   │   ├── exception/                # Global exception handling
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── Custom exception classes
│   │   └── model/
│   │       ├── common/               # ApiResponse, PaginationResponse
│   │       ├── constant/             # SecurityConstants, etc.
│   │       └── entity/               # BaseEntity (UUID, timestamps, soft delete)
│   └── pom.xml
│
├── module-auth/                         # Authentication & Authorization Module
│   ├── src/main/java/.../auth/
│   │   ├── config/                   # Auth-specific configuration
│   │   ├── controller/               # AuthController (register, login, refresh)
│   │   ├── mapper/                   # MapStruct mappers (Entity ↔ DTO)
│   │   ├── model/
│   │   │   ├── entity/               # UserEntity, RefreshTokenEntity
│   │   │   ├── request/              # Login, Register, RefreshToken DTOs
│   │   │   ├── response/             # LoginResponse DTO
│   │   │   └── enums/                # Role, TokenType
│   │   ├── repository/               # JPA Repositories with @EntityGraph
│   │   ├── scheduler/                # Token cleanup scheduler
│   │   ├── security/                 # JWT, SecurityContext, Filters
│   │   ├── service/                  # Business logic (AuthService, etc.)
│   │   └── util/                     # JWT utilities
│   └── pom.xml
│
├── module-project/                      # Project Management Module
│   ├── src/main/java/.../project/
│   │   ├── controller/               # ProjectController (CRUD + Search)
│   │   ├── mapper/                   # MapStruct mappers
│   │   ├── model/
│   │   │   ├── entity/               # ProjectEntity, ProjectMemberEntity
│   │   │   ├── request/              # Create, Update, Search DTOs
│   │   │   ├── response/             # ProjectResponse DTO
│   │   │   └── enums/                # ProjectStatus, MemberRole
│   │   ├── repository/               # JPA Repositories
│   │   └── service/                  # Business logic (ProjectService, etc.)
│   └── pom.xml
│
├── pom.xml                              # Parent POM (dependency management)
├── docker-compose.yml                   # PostgreSQL + Redis containers
├── .env.example                         # Environment variables template
├── mvnw / mvnw.cmd                      # Maven wrapper
└── README.md
```

### Module Responsibilities

**module-app**: 
- Application entry point and startup
- Global configuration (Security, OpenAPI, etc.)
- Database migrations (Flyway)
- Orchestrates all other modules

**module-core**: 
- Shared utilities and common functionality
- Base entities and DTOs
- Global exception handling
- Custom annotations and aspects

**module-auth**: 
- User authentication (register, login)
- JWT token generation and validation
- Refresh token management
- Security configuration

**module-project**: 
- Project CRUD operations
- Project member management
- Project search with pagination

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6.0** or higher (or use the included Maven wrapper)
- **Docker** and **Docker Compose** (for running PostgreSQL and Redis)
- **Git** (for version control)

### Optional
- **Postman** or **Thunder Client** (for API testing)
- **DBeaver** or **pgAdmin** (for database management)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/task-management-system.git
cd task-management-system
```

### 2. Configure Environment Variables

Copy the example environment file and configure it:

```bash
cp .env.example .env
```

Edit `.env` and set the required values:

```dotenv
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5433/local_db
DB_USERNAME=local_user
DB_PASSWORD=local_password

# JWT Configuration (REQUIRED)
JWT_SECRET_KEY=your-256-bit-secret-key-here
JWT_ACCESS_TOKEN_EXPIRATION_IN_SECOND=3600
JWT_REFRESH_TOKEN_EXPIRATION_IN_SECOND=604800000
```

**Important**: Generate a secure `JWT_SECRET_KEY` (at least 256 bits). You can use:
```bash
openssl rand -base64 32
```

### 3. Start Docker Containers

The project uses Docker Compose to orchestrate PostgreSQL and Redis services:

```bash
docker-compose up -d
```

This will start:
- **PostgreSQL 16.11**: Accessible at `localhost:5433`
  - Database: `local_db`
  - User: `local_user`
  - Password: `local_password`
- **Redis**: Accessible at `localhost:6379`

To verify containers are running:

```bash
docker-compose ps
```

### 4. Build the Project

Using Maven wrapper (recommended):

```bash
./mvnw clean install
```

Or using Maven directly:

```bash
mvn clean install
```

This will:
- Compile all modules (core, auth, project, app)
- Run tests
- Generate MapStruct implementations
- Process Lombok annotations
- Package the application

### 5. Run the Application

Using Maven wrapper:

```bash
./mvnw spring-boot:run -pl module-app
```

Or using Maven directly:

```bash
mvn spring-boot:run -pl module-app
```

Or run the JAR directly:

```bash
java -jar module-app/target/module-app-0.0.1-SNAPSHOT.jar
```

The application will start at: `http://localhost:8080`

## ⚙️ Configuration

### Application Properties

The main configuration file is located at `module-app/src/main/resources/application.properties`:

```properties
# Application Configuration
spring.application.name=task-management-system

# Environment variables support
spring.config.import=optional:file:.env[.properties]

# Database Configuration
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5433/local_db}
spring.datasource.username=${DB_USERNAME:local_user}
spring.datasource.password=${DB_PASSWORD:local_password}
spring.datasource.driver-class-name=org.postgresql.Driver

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

# JWT Configuration
jwt.secret-key=${JWT_SECRET_KEY:}
jwt.access-token-expiration-in-second=${JWT_ACCESS_TOKEN_EXPIRATION_IN_SECOND:3600}
jwt.refresh-token-expiration-in-second=${JWT_REFRESH_TOKEN_EXPIRATION_IN_SECOND:604800000}

# OpenAPI Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method

# Token Cleanup Scheduler (Daily at 2 AM)
task.management.system.cron.clean-up-expired-and-revoked-tokens.expression=0 0 2 * * *

# Refresh Token Configuration
task.management.system.refresh-token.stale-time-in-days=7
task.management.system.refresh-token.cleanup-batch-size=100
```

### Environment Variables

You can override properties using environment variables or `.env` file:

```bash
# Database
export DB_URL=jdbc:postgresql://your-host:5433/your_db
export DB_USERNAME=your_user
export DB_PASSWORD=your_password

# JWT (Required for production)
export JWT_SECRET_KEY=your-secure-secret-key
export JWT_ACCESS_TOKEN_EXPIRATION_IN_SECOND=3600
export JWT_REFRESH_TOKEN_EXPIRATION_IN_SECOND=604800000
```

## 🗄️ Database Setup

### Flyway Migrations

Database migrations are automatically executed on application startup. Migration files are located in `module-app/src/main/resources/db/migration/`:

- **V1__initial_database.sql**: Creates UUID extension and trigger functions for timestamp management
- **V2__create_users_table.sql**: Creates the users table with indexes and triggers
- **V3__create_refresh_token_table.sql**: Creates the refresh tokens table for JWT management
- **V4__create_projects_table.sql**: Creates the projects table
- **V5__create_project_members_table.sql**: Creates the project members table with relationships

### Manual Migration (if needed)

To repair Flyway schema history (useful if migrations fail):

```bash
./mvnw flyway:repair -pl module-app
```

To validate migrations:

```bash
./mvnw flyway:validate -pl module-app
```

## 📊 Database Schema

### Users Table

```sql
CREATE TABLE users(
    id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
```

### Refresh Tokens Table

```sql
CREATE TABLE refresh_tokens(
    id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    token VARCHAR(500) UNIQUE NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
```

### Projects Table

```sql
CREATE TABLE projects(
    id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    owner_id UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
```

### Project Members Table

```sql
CREATE TABLE project_members(
    id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    project_id UUID NOT NULL REFERENCES projects(id),
    user_id UUID NOT NULL REFERENCES users(id),
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE,
    UNIQUE(project_id, user_id)
);
```

### Base Entity Fields

All entities extend `BaseEntity` and include:
- `id` (UUID): Primary key
- `created_at` (TIMESTAMP): Automatically set on creation
- `updated_at` (TIMESTAMP): Automatically updated on modification
- `deleted_at` (TIMESTAMP): Used for soft deletes

## 🎮 Running the Application

### Development Mode

```bash
./mvnw spring-boot:run -pl module-app
```

This enables:
- Hot reload with DevTools
- Debug mode
- Detailed logging

### Production Mode

```bash
./mvnw clean package
java -jar module-app/target/module-app-0.0.1-SNAPSHOT.jar
```

### With Custom Port

```bash
./mvnw spring-boot:run -pl module-app -Dspring-boot.run.arguments="--server.port=8081"
```

### Running Tests

Run all tests across all modules:

```bash
./mvnw test
```

Run tests for a specific module:

```bash
./mvnw test -pl module-auth
```

### Building Specific Modules

Build only the core module:

```bash
./mvnw clean install -pl module-core
```

Build multiple modules:

```bash
./mvnw clean install -pl module-core,module-auth
```

## 📚 API Documentation

### Base URL

```
http://localhost:8080/api/v1
```

### Swagger UI & OpenAPI

The API documentation is automatically generated and available at:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Specification**: `http://localhost:8080/api-docs`
- **OpenAPI JSON**: `http://localhost:8080/api-docs.json`

The Swagger UI provides an interactive interface to:
- View all available endpoints
- Test API requests directly
- See request/response schemas
- Review authentication requirements
- Explore error responses

### Authentication Endpoints

#### 1. Register a New User
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePassword123!",
  "fullName": "John Doe"
}
```

#### 2. Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePassword123!"
}
```

Response:
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

#### 3. Refresh Token
```http
POST /api/v1/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Project Management Endpoints

All project endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-access-token>
```

#### 1. Create a Project
```http
POST /api/v1/projects
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "My New Project",
  "description": "Project description here",
  "status": "ACTIVE"
}
```

#### 2. Get Project by ID
```http
GET /api/v1/projects/{projectId}
Authorization: Bearer <token>
```

#### 3. Search Projects (with pagination)
```http
GET /api/v1/projects?page=0&size=10&sort=createdAt,desc&name=search-term
Authorization: Bearer <token>
```

Query Parameters:
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)
- `sort`: Sort field and direction (e.g., `createdAt,desc`)
- `name`: Filter by project name (optional)
- `status`: Filter by project status (optional)

#### 4. Update a Project
```http
PUT /api/v1/projects/{projectId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Updated Project Name",
  "description": "Updated description",
  "status": "COMPLETED"
}
```

#### 5. Delete a Project (Soft Delete)
```http
DELETE /api/v1/projects/{projectId}
Authorization: Bearer <token>
```

### Response Format

All API responses follow a consistent format:

**Success Response:**
```json
{
  "status": 200,
  "message": "Success",
  "data": { ... }
}
```

**Error Response:**
```json
{
  "status": 400,
  "message": "Error message",
  "errors": {
    "field": "error description"
  }
}
```

**Pagination Response:**
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "content": [...],
    "page": 0,
    "size": 10,
    "totalElements": 50,
    "totalPages": 5,
    "last": false
  }
}
```

## 🏗️ Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│     REST Controllers (HTTP)         │
├─────────────────────────────────────┤
│    Service Layer (Business Logic)   │
├─────────────────────────────────────┤
│  Repository Layer (Data Access)     │
├─────────────────────────────────────┤
│    Database (PostgreSQL)            │
└─────────────────────────────────────┘
```

### Key Components

- **Controller**: Handles HTTP requests and responses
- **Service**: Contains business logic and validations
- **Repository**: Interfaces with the database via JPA
- **Entity**: JPA entities representing database tables
- **DTO**: Data Transfer Objects for request/response handling

## 🔍 Key Features

### Base Entity Pattern

All entities inherit from `BaseEntity` which provides:
- Automatic UUID generation
- Creation and update timestamps
- Soft delete support
- Common utility methods

### Soft Delete Implementation

Entities support soft deletes through the `deleted_at` field:
- Records are not physically deleted
- `deleted_at` is set to the current timestamp
- Queries filter out soft-deleted records

### Database Triggers

Automatic timestamp management:
- `created_at`: Set once on insertion
- `updated_at`: Automatically updated on any modification

## 🔧 Troubleshooting

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Database Connection Issues

```bash
# Check if PostgreSQL container is running
docker-compose ps

# View PostgreSQL logs
docker-compose logs db

# Restart containers
docker-compose restart
```

### Migration Failures

```bash
# Reset database (WARNING: clears all data)
docker-compose down -v
docker-compose up -d

# Then rebuild and run
./mvnw clean install
./mvnw spring-boot:run
```

### Rebuild Application Cache

```bash
./mvnw clean
./mvnw compile
./mvnw spring-boot:run
```

## 📝 Logging

Logging configuration can be set in `application.properties`:

```properties
# Log Levels
logging.level.root=INFO
logging.level.duy.personalproject.taskmanagementsystem=DEBUG
logging.level.org.springframework=INFO
```

## 🚦 Health Check

The application provides health endpoints:

```bash
# Check application health
curl http://localhost:8080/actuator/health
```

## 📦 Building for Production

### Create JAR Package

```bash
./mvnw clean package -DskipTests
```

### Docker Build (Optional)

If you have a Dockerfile configured:

```bash
docker build -t task-management-system:latest .
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Create a feature branch (`git checkout -b feature/amazing-feature`)
2. Commit your changes (`git commit -m 'Add amazing feature'`)
3. Push to the branch (`git push origin feature/amazing-feature`)
4. Open a Pull Request

## 📄 License

This project is part of a personal learning initiative.

## 👨‍💻 Author

**Duy Le**

## 📞 Support

For issues and questions, please open an issue on the repository or contact the author.

## 🔗 Useful Links

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Redis Documentation](https://redis.io/documentation)

---

**Last Updated**: January 3, 2026

**Version**: 0.0.1-SNAPSHOT

