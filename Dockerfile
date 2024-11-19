# Use a base image with Java
FROM openjdk:11-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/spring-security-by-toptal-*.jar app.jar

# Expose the port your application runs on
EXPOSE 8082

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]