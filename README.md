# Baymak Service Backend

Baymak Service Backend is a Spring Boot application designed to support after-sales service operations.  
It provides secure, role-based APIs for customer requests, device management, appointment scheduling, technician workflows, and service reporting.

## Key Features

- JWT-based authentication and authorization
- Role-based access control (`ADMIN`, `TECHNICIAN`, `CUSTOMER`)
- Appointment lifecycle management
- Device, user, and technician management
- Service request and service report workflows
- Swagger/OpenAPI integration for API exploration
- Layered architecture with Spring Data JPA and MySQL

## Technology Stack

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- Lombok
- springdoc-openapi (Swagger UI)
- Thymeleaf (landing page)

## Project Structure

Base package: `com.baymak.backend`

- `controller/` - REST API endpoints
- `service/` and `service/impl/` - business logic and implementations
- `repository/` - data access layer
- `model/` - JPA entities and enums
- `dto/` - request/response contracts
- `security/` - JWT provider, authentication filter, user details service
- `config/` - security and OpenAPI configuration
- `exception/` - global and domain-level exception handling

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.9+
- MySQL 8+

### 1) Clone the repository

```bash
git clone https://github.com/omermk/baymak-service-backend.git
cd baymak-service-backend
```

### 2) Configure application properties

Update `src/main/resources/application.properties` for your local environment:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `jwt.secret`

Security recommendation:
- Do not commit credentials or secrets.
- Use environment variables or a secret manager in shared environments.

### 3) Run the application

```bash
./mvnw spring-boot:run
```

The application starts by default at: `http://localhost:8080`

## API Documentation

When the service is running, Swagger UI is available at:

- `http://localhost:8080/swagger-ui/index.html`

## Main API Domains

- Authentication: `/api/auth/**`
- Appointments: `/api/appointments/**`
- Devices: `/api/devices/**`
- Service Requests: `/api/requests/**`
- Service Reports: `/api/service-reports/**`
- Technicians: `/api/technicians/**`
- Users: `/api/users/**`

## Additional Documentation

- `PROJE_MIMARI.md` - detailed architecture guide
- `PROJE_DIAGRAM.md` - diagrams for layers, flows, and entity relationships

