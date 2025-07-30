<h1 align="center">🎯 Biddora – Auction Web App (Spring Boot Backend)</h1>
<p align="center">
  A secure, scalable, and production-ready backend for an online auction platform built with Java & Spring Boot.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-Backend-success?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql" />
  <img src="https://img.shields.io/badge/JWT-Security-orange?style=for-the-badge&logo=jsonwebtokens" />
</p>

---

## 🚀 Features

<ul>
  <li>🔒 <strong>JWT Authentication & Role-Based Access Control</strong> (USER / ADMIN)</li>
  <li>📦 Clean <strong>DTO-Service-Controller</strong> architecture</li>
  <li>📑 <strong>CRUD</strong> operations with custom validation and business rules</li>
  <li>📤 Auction logic with real-time-ready architecture (WebSocket planned)</li>
  <li>📅 Date constraints: auction start must be in the future, end date must be after start — with planned support for multiple time zones</li>
  <li>🏆 Auto-winner selection after auction ends</li>
  <li>🧪 Ready for frontend integration (React)</li>
</ul>

---

## 🧩 Technologies Used

| Layer            | Stack                                      |
|------------------|--------------------------------------------|
| Backend          | Java 17, Spring Boot 3                     |
| Security         | Spring Security, JWT                       |
| Database         | PostgreSQL, Spring Data JPA                |
| Build Tool       | Maven                                      |
| Documentation    | Swagger / OpenAPI                          |
| Deployment Ready | Docker (planned), Railway/Render support   |

---

## 📁 Project Structure

```bash
src/
├── config/           # Security configurations, JWT filters
├── controller/       # REST controllers
├── dto/              # Request/response objects
├── entity/           # JPA entities (User, Product, Bid, Notification)
├── exception/        # Global error handling
├── repository/       # Spring Data JPA interfaces
├── security/         # JWT utils and filters
├── service/          # Business logic interfaces
├── service/impl/     # Business logic implementations
├── util/             # Helpers (AI, file generation, etc.)
