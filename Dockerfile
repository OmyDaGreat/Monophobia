# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:21-jdk AS build

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Make gradlew executable
RUN chmod +x gradlew

# Install Node.js and Playwright dependencies
RUN apt-get update && apt-get install -y \
    curl \
    libglib2.0-0 \
    libnss3 \
    libnspr4 \
    libdbus-1-3 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdrm2 \
    libatspi2.0-0 \
    libx11-6 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libxcb1 \
    libxkbcommon0 \
    libpango-1.0-0 \
    libcairo2 \
    libasound2t64 && \
    curl -fsSL https://deb.nodesource.com/setup_16.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g playwright && \
    playwright install-deps

# Build the project (adjust the build command as needed)
RUN ./gradlew kobwebExport

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
