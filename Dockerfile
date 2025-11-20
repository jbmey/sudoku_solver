# Multi-stage Dockerfile for Spring Boot application deployment on Render
# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster build, tests should run in CI)
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine

# Install dumb-init and curl for health checks
RUN apk add --no-cache dumb-init curl

# Create a non-root user for security
RUN addgroup -g 1000 appgroup && \
    adduser -D -s /bin/sh -u 1000 -G appgroup appuser

# Set working directory
WORKDIR /app

# Copy the jar file from builder stage
COPY --from=builder /app/target/sudoku_solver-0.0.1.jar app.jar

# Change ownership to appuser
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port (Render uses PORT environment variable)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Use dumb-init to handle signals properly
ENTRYPOINT ["dumb-init", "--"]

# Run the application with optimized JVM settings for containers
CMD ["java", \
     "-server", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-XX:+UseG1GC", \
     "-XX:+UseStringDeduplication", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Dserver.port=${PORT:-8080}", \
     "-jar", "app.jar"]