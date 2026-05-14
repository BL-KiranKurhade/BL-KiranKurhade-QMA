# ================================================================
# Auth Service  |  Port 8083
# Handles JWT registration, login, and user management
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

COPY --from=builder /app/target/auth-service-*.jar app.jar

EXPOSE 8083

# Runtime environment variables (overridden by docker-compose)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=docker
ENV EUREKA_URI=http://eureka-server:8761/eureka/
ENV MYSQL_HOST=mysql-2cee1feb-qma-database.h.aivencloud.com
ENV MYSQL_PORT=24566
ENV MYSQL_DATABASE=defaultdb
ENV MYSQL_USER=avnadmin
ENV MYSQL_PASSWORD=AVNS_BBXDphsKwwauXsNRdSu
ENV JWT_SECRET=QMAAuthServiceSecretKey2024ForJWTTokenSigningMustBe256BitsLong
ENV JWT_EXPIRATION=86400000
ENV CORS_ALLOWED_ORIGINS=http://localhost:3000

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -f http://localhost:8083/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
