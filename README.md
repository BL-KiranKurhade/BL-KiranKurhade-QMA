# UC21 - Microservices Architecture for Quantity Measurement App

## Architecture
```
Client → API Gateway (8080) → [measurement-service (8081)]
                             → [conversion-service  (8082)]
                  ↓
           Eureka Server (8761) — Service Registry
```

## Services
| Service              | Port | Description                        |
|----------------------|------|------------------------------------|
| eureka-server        | 8761 | Service registry dashboard         |
| api-gateway          | 8080 | Single entry point, load balancing |
| measurement-service  | 8081 | CRUD for measurements (JPA + H2)   |
| conversion-service   | 8082 | Unit conversion logic              |

## Startup Order
```bash
# 1. Start Eureka
cd eureka-server && mvn spring-boot:run

# 2. Start API Gateway
cd api-gateway && mvn spring-boot:run

# 3. Start Measurement Service
cd measurement-service && mvn spring-boot:run

# 4. Start Conversion Service
cd conversion-service && mvn spring-boot:run
```

## Test
```bash
# Via API Gateway:
curl http://localhost:8080/api/measurements
curl "http://localhost:8080/api/convert?value=1&from=ft&to=in&category=LENGTH"

# Eureka Dashboard:
open http://localhost:8761
```
