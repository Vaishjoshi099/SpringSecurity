# Use an official OpenJDK runtime as a base image
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the application .jar file into the container
COPY target/*.jar /app/*.jar

# Expose port 8080 (or the port your app uses)
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "/app/*.jar"]
