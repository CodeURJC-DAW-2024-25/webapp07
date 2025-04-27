# Stage 1: Build Angular application
FROM node:20-alpine AS angular-builder

WORKDIR /app

# Copy package files first for better layer caching
COPY frontend/package*.json ./
RUN npm install

# Copy assets first
COPY frontend/src/assets/ ./src/assets/

# Copy the rest of the Angular app
COPY frontend/ .
RUN npx ng build --configuration production

# Stage 2: Build Spring Boot application
FROM maven:3.9.9-eclipse-temurin-21 AS maven-builder
WORKDIR /volteretaCroqueta

# Copy Maven files
COPY backend/pom.xml .
RUN mvn dependency:go-offline

# Copy backend source
COPY backend/src ./src

# Copy Angular build output from previous stage
COPY --from=angular-builder /app/dist/frontend ./src/main/resources/static/spa

# Build the Spring Boot application
RUN mvn clean package -DskipTests -Pproduction

# Final stage: Runtime
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the built JAR from the maven-builder stage
COPY --from=maven-builder /volteretaCroqueta/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application runs on [8443]
EXPOSE 8443

# Entrypoint command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]