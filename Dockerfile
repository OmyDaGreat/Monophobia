# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app/site

# Copy the current directory contents into the container at /app/site
COPY . /app/site

# Expose the specified port (default is 8080)
ARG PORT=8080
EXPOSE ${PORT}

# Define environment variable for the port
ENV PORT=${PORT}

# Run the application
CMD ["java", "-jar", ".kobweb/server/server.jar"]
