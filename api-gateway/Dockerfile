# ================================================================
# API Gateway  |  Port 8080
# Spring Cloud Gateway - single entry point for all services
# Multi-stage build: Maven builder -> JRE runtime
# ================================================================

# ---- Stage 1: Build ------------------------------------------------
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests --no-transfer-progress \
    -Dmaven.repo.local=/root/.m2/repository

# ---- Stage 2: Runtime ----------------------------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime

RUN apk add --no-cache curl

WORKDIR /app

RUN addgroup -S qma && adduser -S qma -G qma
USER qma

COPY --from=builder /app/target/api-gateway-*.jar app.jar

# Main gateway port (all frontend traffic comes through here)
EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=docker
ENV EUREKA_URI=http://eureka-server:8761/eureka/
ENV CORS_ALLOWED_ORIGINS=http://localhost:3000

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS \
    -Deureka.client.service-url.defaultZone=${EUREKA_URI} \
    -Dspring.cloud.gateway.globalcors.corsConfigurations.[/**].allowedOrigins=${CORS_ALLOWED_ORIGINS} \
    -jar app.jar"]
