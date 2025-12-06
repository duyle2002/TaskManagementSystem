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

The Task Management System is a full-featured REST API that allows users to:
- Create, read, update, and delete tasks
- Manage user accounts and authentication
- Organize tasks by categories and priorities
- Track task completion status
- Support soft deletes for data integrity

## 🛠 Tech Stack

### Backend
- **Framework**: Spring Boot 3.4.12
- **Java Version**: 17
- **Build Tool**: Maven

### Database & Cache
- **Database**: PostgreSQL 16.11 (Alpine)
- **Cache**: Redis (Latest)
- **Migrations**: Flyway Database

### Libraries & Tools
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security
- **Email**: Spring Mail
- **Validation**: Spring Validation
- **Project Lombok**: For reducing boilerplate code
- **PostgreSQL Driver**: For database connectivity

### Development
- **Spring Boot DevTools**: For hot reload
- **Testing**: JUnit, Spring Security Test

## 📁 Project Structure

```
task-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── duy/personalproject/taskmanagementsystem/
│   │   │       ├── TaskManagementSystemApplication.java
│   │   │       ├── config/           # Configuration classes
│   │   │       ├── controller/       # REST endpoints
│   │   │       ├── model/
│   │   │       │   ├── common/       # BaseEntity and shared models
│   │   │       │   ├── entity/       # JPA entity classes
│   │   │       │   ├── enums/        # Enumerations
│   │   │       │   ├── request/      # DTO request objects
│   │   │       │   └── response/     # DTO response objects
│   │   │       ├── repository/       # Data access layer
│   │   │       ├── scheduler/        # Scheduled tasks
│   │   │       ├── service/          # Business logic layer
│   │   │       └── util/             # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── banner.txt
│   │       └── db/migration/         # Flyway migrations
│   └── test/
│       └── java/...                  # Test classes
├── docker-compose.yml                # Docker container setup
├── pom.xml                           # Maven dependencies
├── mvnw                              # Maven wrapper (Unix)
└── README.md                         # This file
```

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

### 2. Start Docker Containers

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

### 3. Build the Project

Using Maven wrapper (recommended):

```bash
./mvnw clean install
```

Or using Maven directly:

```bash
mvn clean install
```

### 4. Run the Application

Using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or using Maven directly:

```bash
mvn spring-boot:run
```

The application will start at: `http://localhost:8080`

## ⚙️ Configuration

### Application Properties

The main configuration file is located at `src/main/resources/application.properties`:

```properties
# Application Configuration
spring.application.name=task-management-system

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/local_db
spring.datasource.username=local_user
spring.datasource.password=local_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

### Environment Variables

You can override properties using environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://your-host:5433/your_db
export SPRING_DATASOURCE_USERNAME=your_user
export SPRING_DATASOURCE_PASSWORD=your_password
```

## 🗄️ Database Setup

### Flyway Migrations

Database migrations are automatically executed on application startup. Migration files are located in `src/main/resources/db/migration/`:

- **V1__initial_database.sql**: Creates extensions and trigger functions
- **V2__create_users_table.sql**: Creates the users table with indexes and triggers

### Manual Migration (if needed)

To repair Flyway schema history (useful if migrations fail):

```bash
./mvnw flyway:repair
```

To validate migrations:

```bash
./mvnw flyway:validate
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
)
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
./mvnw spring-boot:run
```

This enables:
- Hot reload with DevTools
- Debug mode
- Detailed logging

### Production Mode

```bash
./mvnw clean package
java -jar target/task-management-system-0.0.1-SNAPSHOT.jar
```

### With Custom Port

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

## 📚 API Documentation

### Base URL

```
http://localhost:8080/api
```

### Authentication

The API uses Spring Security. Include authentication headers in your requests.

### Example Endpoints

(Add your specific endpoints here as you develop them)

```
GET    /api/tasks              - Get all tasks
POST   /api/tasks              - Create a new task
GET    /api/tasks/{id}         - Get a specific task
PUT    /api/tasks/{id}         - Update a task
DELETE /api/tasks/{id}         - Delete a task

GET    /api/users              - Get all users
POST   /api/users              - Create a new user
GET    /api/users/{id}         - Get a specific user
PUT    /api/users/{id}         - Update a user
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

**Duy Pham**

## 📞 Support

For issues and questions, please open an issue on the repository or contact the author.

## 🔗 Useful Links

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Redis Documentation](https://redis.io/documentation)

---

**Last Updated**: December 6, 2025

**Version**: 0.0.1-SNAPSHOT

