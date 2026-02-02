# Biddora – Real-Time Auction Backend

## Overview
Biddora is a backend system for a real-time auction platform where users place live bids on products and receive instant updates via WebSocket connections.
The application is designed with a strong focus on security, data consistency, and performance under concurrent access.

It implements JWT-based authentication and role-based authorization using Spring Security, ensuring secure access to both REST APIs and WebSocket sessions.

---

## Core Features
- Real-time bidding using WebSockets
- Secure REST APIs with Spring Security (JWT)
- Role-based access control (admin / user)
- Automated auction winner determination
- User-scoped resources (favorites, ratings)
- Redis caching for frequently accessed data
- API documentation with SpringDoc OpenAPI

---

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security (JWT)
- Spring WebSocket
- Spring Data JPA (Hibernate)

### Data & Infrastructure
- PostgreSQL
- Redis
- Docker

### Testing & Documentation
- JUnit 5
- Mockito
- SpringDoc OpenAPI (Swagger)

---

## Architecture Overview
The application follows a layered architecture:

- Controller layer – Handles HTTP and WebSocket requests
- Service layer – Contains business logic and validation rules
- Repository layer – Data persistence with JPA
- Security layer – JWT authentication and role-based authorization
- Caching layer – Redis for optimized read performance

This structure ensures clear separation of concerns, maintainability, and testability.

---

## Security
- JWT-based authentication
- Role-based authorization (admin / user)
- Secured REST endpoints
- Secured WebSocket connections
- Validation and centralized exception handling

---

## Performance Considerations
- Redis caching is used to reduce database load for frequently accessed product and auction data
- Bid validation logic ensures data consistency during concurrent bidding
- Optimized query usage through JPA and pagination

---

## Running the Application

### Prerequisites
- Java 17
- Docker & Docker Compose

### Environment Variables
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/biddora  
SPRING_DATASOURCE_USERNAME=postgres  
SPRING_DATASOURCE_PASSWORD=postgres

JWT_SECRET=your_jwt_secret  
REDIS_HOST=localhost  
REDIS_PORT=6379

### Start with Docker
docker-compose up --build

The backend will be available at:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

---

## Testing
- Unit and integration tests implemented using JUnit 5 and Mockito
- Focus on service-layer logic and security-related components

---

## Project Status
This project represents a production-oriented backend system designed to reflect real-world requirements such as authentication, authorization, concurrency handling, and performance optimization.

---

## Author
Mirza Felić  
Backend Engineer (Java / Spring)
