# ================================================================
# API Gateway  |  Binds to $PORT (injected by Render at runtime)
# Spring Cloud Gateway — reactive WebFlux, single entry-point
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

EXPOSE 8080

# All secrets/config injected by Render at runtime — nothing hardcoded
ENV JAVA_OPTS="-XX:+UseContainerSupport -Xmx384m -Xms128m -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=render

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
