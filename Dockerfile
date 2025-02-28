# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:21-jdk AS build

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Make gradlew executable
RUN chmod +x gradlew

# Build the project (adjust the build command as needed)
RUN ./gradlew build

# Use an official Eclipse Temurin runtime as a parent image for the runtime
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app/site

# Copy the build artifacts from the build stage
COPY --from=build /app/site/.kobweb/server /app/site/.kobweb/server

# Expose the specified port (default is 3333)
ARG PORT=3333
EXPOSE ${PORT}

# Define environment variable for the port
ENV PORT=${PORT}

# Run the application
CMD ["java", "-jar", ".kobweb/server/server.jar"]
