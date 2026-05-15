# ================================================================
# Eureka Server  |  Port 8761
# Multi-stage build: Maven builder -> JRE runtime
# ================================================================

# ---- Stage 1: Build ------------------------------------------------
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy service pom and source
COPY pom.xml .
COPY src ./src

# Build, skip tests for faster image build
RUN mvn clean package -DskipTests --no-transfer-progress \
    -f pom.xml \
    -Dmaven.repo.local=/root/.m2/repository

# ---- Stage 2: Runtime ----------------------------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime

# Install curl for health checks
RUN apk add --no-cache curl

WORKDIR /app

# Create non-root user for security
RUN addgroup -S qma && adduser -S qma -G qma
USER qma

# Copy jar from builder
COPY --from=builder /app/target/eureka-server-*.jar app.jar

# Expose Eureka dashboard + registry port
EXPOSE 8761

# JVM tuning for containers - Increased memory for stability
ENV JAVA_OPTS="-XX:+UseContainerSupport -Xmx384m -Xms128m -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
