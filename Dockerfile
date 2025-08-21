# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim as builder

ARG VERSION

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build artifact (JAR file) from the local machine
COPY target/opposite-treasure-$VERSION.jar opposite-treasure-$VERSION.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/opposite-treasure-$VERSION.jar"]