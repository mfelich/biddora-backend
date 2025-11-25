# Biddora - *real-time auction platform*

## Project Overview
Biddora is a real-time auction backend platform that enables live bidding on products. The system provides real-time bid updates and highest bid tracking through WebSocket connections. JWT token-based security handles user authentication and authorization for all API requests. Additional features include product favorites management and user rating system.

## Tech Stack
- Java 17, Spring Boot 3.5.3
- Spring Security + JWT
- WebSocket
- PostgreSQL, Spring Data JPA
- Spring Doc OpenAPI (Swagger)
- Docker, Maven

## Key Features
- Real-time bidding via WebSocket
- JWT-based authentication & authorization
- Auction management with automatic winner determination
- Bid validation
- Custom exception handling through GlobalExceptionHandler
- Rating and Favorite system

## Installation & Setup 
```bash
git clone "https://github.com/mfelich/biddora-backend.git"
docker compose up
```

## API Documentation
```bash
http://localhost:8080/swagger-ui/index.html
```
