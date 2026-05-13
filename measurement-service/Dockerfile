# ================================================================
# Measurement Service  |  Port 8084
# Stores and retrieves measurement records
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

COPY --from=builder /app/target/measurement-service-*.jar app.jar

EXPOSE 8084

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=docker
ENV EUREKA_URI=http://eureka-server:8761/eureka/
ENV MYSQL_HOST=mysql
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=qma_measurements
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=root

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -f http://localhost:8084/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS \
    -Dspring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DATABASE}?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true \
    -Dspring.datasource.username=${MYSQL_USER} \
    -Dspring.datasource.password=${MYSQL_PASSWORD} \
    -Deureka.client.service-url.defaultZone=${EUREKA_URI} \
    -jar app.jar"]
