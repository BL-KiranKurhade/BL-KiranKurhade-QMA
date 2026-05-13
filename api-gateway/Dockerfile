# ================================================================
# API Gateway  |  Port 8080
# Spring Cloud Gateway - single entry point for all services
# Multi-stage build: Maven builder -> JRE runtime
# ================================================================

# ---- Stage 1: Build ------------------------------------------------
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy the service pom and source
COPY pom.xml .
COPY src ./src

# Build the module
RUN mvn clean package -DskipTests --no-transfer-progress \
    -Dmaven.repo.local=/root/.m2/repository

# ---- Stage 2: Runtime ----------------------------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime

RUN apk add --no-cache curl

WORKDIR /app

RUN addgroup -S qma && adduser -S qma -G qma
USER qma

# Copy from the builder target directory
COPY --from=builder /app/target/api-gateway-*.jar app.jar

# Main gateway port (all frontend traffic comes through here)
EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=docker

# Note: EUREKA_URI and CORS_ALLOWED_ORIGINS must be set as environment variables in Render
# ENV EUREKA_URI=
# ENV CORS_ALLOWED_ORIGINS=

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
