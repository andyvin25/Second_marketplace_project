# AGENTS.md

## Build Commands
- Build: `./mvnw clean package`
- Run: `./mvnw spring-boot:run`
- Single test: `./mvnw test -Dtest=ClassName`
- Test with profile: `./mvnw test -Dspring.profiles.active=test`

## Tech Stack
- Spring Boot 3.5.11, Java 21, PostgreSQL, Redis, JWT (jjwt 0.12.6)
- Maven wrapper: `./mvnw`

## Architecture
- **Vertical slicing**: each feature in `src/main/java/com/marketplace/{Feature}/` has `api/` and `domain/` subdirectories
- Service layer uses field injection (`@Autowired`); controller uses constructor injection with `@AllArgsConstructor`
- Default ID generator: `@Id @GeneratedValue private String id;`
- Entities with `created_at`/`updated_at` extend `Auditable`

## Configuration
- Active profile: `dev` (set in `application.properties`)
- Environment variables required: `DB_USERNAME`, `DB_PASSWORD`, `DB_DATASOURCE_URL`, `REDIS_HOST`, `REDIS_PORT`, `ACCESS_TOKEN`, `REFRESH_TOKEN`, `FILE_UPLOAD_PATH`, `PATH_FILE_CSV`, `ADMIN_NAME`, `ADMIN_PASSWORD`, `ADMIN_EMAIL`
- Test uses H2 in-memory database (`application-test.properties`)
- Swagger UI: `/swagger-ui/index.html`

## Code Conventions
- DTOs use `record` with `@JsonNaming(SnakeCaseStrategy.class)`
- Services: field injection (`@Autowired`), return `void` or entities, throw existing exceptions
- Controllers: return `ResponseHandler.generateResponse(message, status, data)`
- Repositories use JPQL; no soft delete by default

## Existing Instruction Files
- `ai_script.md` - detailed code generation rules (vertical slicing, DTO patterns, service/controller conventions)
- `securityFeautresDoc.md` - security notes (rate limiting, credential enumeration)

## Reference
- Main class: `com.marketplace.MarketplaceApplication.java`
- Package structure: `src/main/java/com/marketplace/{Auth,Banner,CategoryManagement,StoreManagement,UserAccountManagement,...}/`