# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY target/example-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (change it if your app runs on a different port)
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
