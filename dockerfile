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
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy file jar từ stage build
COPY --from=builder /app/target/*.jar app.jar

# Railway sẽ inject PORT -> ta map vào Spring Boot
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
