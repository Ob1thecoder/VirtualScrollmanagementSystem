FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY src src

# Build the application
RUN ./gradlew clean build -x test

# Copy the built JAR file (use a more specific name)
RUN cp build/libs/*.jar app.jar

# Expose the Spring Boot server port
EXPOSE 8081

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
