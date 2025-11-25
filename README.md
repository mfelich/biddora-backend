<h1>Biddora - <i>real-time auction platform</i></h1>

<h2>Project Overview</h2>
<p>Biddora is a real-time auction platform backend that enables live bidding on products. The system provides real-time bid updates and highest bid tracking through WebSocket connections. JWT token-based security handles user authentication and authorization for all API requests. Additional features include product favorites management and user rating system.</p>

<h2>Tech stack</h2>
<ul>
    <li>Java 17, Spring Boot 3.5.3</li>
    <li>Spring Security + JWT</li>
    <li>WebSocket</li>
    <li>PostgreSQL, Spring Data JPA</li>
    <li>Spring Doc OpenAPI (Swagger)</li>
    <li>Docker, Maven</li>
</ul>

<h2>Key Features</h2>
<ul>
    <li>Real-time bidding via WebSocket</li>
    <li>JWT-based authentication & authorization</li>
    <li>Auction management with automatic winner determination</li>
    <li>Bid validation</li>
    <li>Custom exception handling through GlobalExceptionHandler</li>
    <li>Rating and Favorite system</li>
</ul>

<h2>Installation & Setup</h2>
```bash
git clone "https://github.com/mfelich/biddora-backend.git"
docker compose up
```

<h2>API Documentation</h2>
```bash
http://localhost:8080/swagger-ui/index.html
```
