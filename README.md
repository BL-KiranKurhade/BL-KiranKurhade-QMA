# UC17 - Spring Boot Backend

## Prerequisites
- Java 11+
- Maven 3.6+

## Run
```bash
mvn spring-boot:run
```

## API Endpoints
| Method | URL                         | Description     |
|--------|-----------------------------|-----------------|
| POST   | /api/measurements           | Create          |
| GET    | /api/measurements           | Get all         |
| GET    | /api/measurements/{id}      | Get by ID       |
| GET    | /api/measurements?category= | Filter          |
| PUT    | /api/measurements/{id}      | Update          |
| DELETE | /api/measurements/{id}      | Delete          |

## Sample Request
```bash
curl -X POST http://localhost:8080/api/measurements \
  -H "Content-Type: application/json" \
  -d '{"value":1.0,"unit":"ft","category":"LENGTH"}'
```
