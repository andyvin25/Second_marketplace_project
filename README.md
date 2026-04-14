# Marketplace Backend API

A Spring Boot 3.5.11 marketplace application with Java 21, PostgreSQL, Redis, and JWT authentication.

## Tech Stack

- **Framework**: Spring Boot 3.5.11
- **Language**: Java 21
- **Database**: PostgreSQL with JPA/Hibernate
- **Cache**: Redis
- **Authentication**: JWT (jjwt 0.12.6)
- **API Documentation**: Swagger UI (`/swagger-ui/index.html`)

## Implemented Features

### Authentication (`/Auth`)
- JWT-based authentication with refresh tokens
- Cors configuration
- Runtime registrar for JWT libraries

### Configuration (`/Config`)
- Cors configuration for frontend access
- Rate limiter with Redis
- Service-level rate limiting (`/RateLimiter`)

### Exception Handling (`/Exception`)
- Centralized exception handling

### User Account Management (`/UserAccountManagement`)
- CRUD operations for user addresses
- Password change functionality
- Email and name updates
- Account deletion

### Store Operating Hours (`/StoreOperatingHours`)
- Create operating hours for sellers
- Retrieve all operating hours
- Get today's operating hours
- Update specific day operating hours

### Category Management (`/CategoryManagement`)
- Create categories
- Retrieve all categories

### Sub Category Management (`/SubCategoryManagement`)
- Create sub-categories
- Retrieve all sub-categories

### Store Management (`/StoreManagement`)
- Buyer-to-seller registration
- Store logo upload/update
- Delete store logo
- Full CRUD for store management

## Configuration

Environment variables required:
- `DB_USERNAME`, `DB_PASSWORD`, `DB_DATASOURCE_URL`
- `REDIS_HOST`, `REDIS_PORT`
- `ACCESS_TOKEN`, `REFRESH_TOKEN`
- `FILE_UPLOAD_PATH`, `PATH_FILE_CSV`
- `ADMIN_NAME`, `ADMIN_PASSWORD`, `ADMIN_EMAIL`

Active profile: `dev` (set in `application.properties`)

## Build & Run

```bash
./mvnw clean package
./mvnw spring-boot:run
```

## API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui/index.html`

## Architecture

- Vertical slicing: each feature in `src/main/java/com/marketplace/{Feature}/`
- Each feature contains `api/` and `domain/` subdirectories