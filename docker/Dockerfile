# Use the official maven/Java 21 image as the base image
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the working directory
COPY ../backend/pom.xml ./pom.xml
COPY ../backend/src ./src

# Build the project and package the application

RUN mvn clean package -DskipTests


# Use the official OpenJDK 21 image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build image into the working directory
COPY --from=build /app/target/*.jar .

# Expose the port that the application runs on [8443]
EXPOSE 8443

# Entrypoint command to run the application
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]