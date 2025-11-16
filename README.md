## Biddora Backend

A backend system for a real-time auction application built with **Spring Boot**, featuring secure bidding, role-based access, and automated auction processing.

## Key Features

* JWT authentication with role-based access control (`USER`, `ADMIN`)
* Real-time bidding via WebSocket (STOMP)
* REST API with Swagger documentation
* Automated auction closing and winner generation
* DTO validation with global exception handling
* Clean layered architecture (controller, service, repo, dto, mapper, config)

## Architecture Structure

```
config       # Security, WebSocket, Jackson
controller   # REST endpoints
service      # Interfaces
serviceImpl  # Business logic
entity       # JPA entities
dto          # Request/response models with validation
mapper       # Entity <-> DTO
repo         # Spring Data JPA repositories
exception    # Custom exceptions + global handler
filter       # JWT filter
handlers     # Additional utilities
```

## Security

* Spring Security + JWT authentication
* Authorization enforced inside service layer
* EntityFetcher used to resolve current user from JWT

## Real-Time Bidding

* WebSocket used to broadcast live bid updates
* Ensures multiple users see instant bid changes

## Auction Lifecycle

1. User creates a product with start/end time and starting price
2. Users place bids (validation ensures each bid is higher)
3. When `endTime` is reached:

   * Auction is closed (`OPEN → CLOSED`)
   * Winner is determined and saved as `AuctionWinner` in PostgreSQL

## Technologies

* Spring Boot
* Spring Security + JWT
* Spring Data JPA (Hibernate)
* PostgreSQL
* WebSocket (STOMP)
* Swagger / OpenAPI
* Lombok

## Running the Application

To run the backend locally using Docker:

```bash
git clone https://github.com/mfelich/biddora-backend.git
cd biddora-backend
docker compose up --build
```

The application is fully containerized using **Docker** and includes:

* Spring Boot backend container
* PostgreSQL database container
* Automatic networking and environment configuration

## Additional Implementation Details

* Pagination and sorting implemented for product and user endpoints
* Service-layer access control (users can edit/delete only their own products)
* Global validation handling using `@ControllerAdvice` and custom `ErrorDetails`
* Fetching of current user and related entities through a dedicated `EntityFetcher` service
* Clean separation between DTO → Mapper → Service → Repository
* Spring Boot 3+ with Java 17+
