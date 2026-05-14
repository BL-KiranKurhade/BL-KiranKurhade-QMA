# ================================================================
# Conversion Service  |  Port 8082
# Handles unit conversion, history, custom units, and favourites
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

COPY --from=builder /app/target/conversion-service-*.jar app.jar

EXPOSE 8082

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=render
ENV EUREKA_URI=http://eureka-server:8761/eureka/
ENV MYSQL_HOST=mysql
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=qma_conversion
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=root
ENV JWT_SECRET=QMAAuthServiceSecretKey2024ForJWTTokenSigningMustBe256BitsLong

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -f http://localhost:8082/api/convert/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
