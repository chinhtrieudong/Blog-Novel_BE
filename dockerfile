# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml và download dependencies trước (để cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy toàn bộ source code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM maven:3.9.6-eclipse-temurin-21
WORKDIR /app

# Copy file jar từ stage build
COPY --from=builder /app/target/*.jar app.jar
# Copy pom.xml và src để có thể chạy flyway commands
COPY pom.xml .
COPY src ./src

# Create startup script
RUN echo '#!/bin/bash\n\
    echo "Running flyway repair..."\n\
    mvn flyway:repair -q || echo "Repair completed or not needed"\n\
    echo "Starting application..."\n\
    java -jar app.jar' > /app/start.sh && chmod +x /app/start.sh

# Railway sẽ inject PORT -> ta map vào Spring Boot
EXPOSE 8080

ENTRYPOINT ["/app/start.sh"]
