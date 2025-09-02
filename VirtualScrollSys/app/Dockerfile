
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/*.jar ./app.jar

# Expose the Spring Boot server port
EXPOSE 8081

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
