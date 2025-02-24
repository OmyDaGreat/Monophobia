# Build stage
FROM eclipse-temurin:21-jdk as builder

# Set working directory
WORKDIR /app

# Set Kobweb version
ENV KOBWEB_CLI_VERSION=0.9.18

# Install required packages
RUN apt-get update && apt-get install -y \
    unzip \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY . .

# Download and setup Kobweb CLI
RUN curl -L -o kobweb.zip https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip \
    && unzip kobweb.zip \
    && chmod +x kobweb-${KOBWEB_CLI_VERSION}/bin/kobweb

# Build the project
RUN ./gradlew build

# Export the site
RUN cd site && \
    ../kobweb-${KOBWEB_CLI_VERSION}/bin/kobweb export --layout static --notty

# Serve stage
FROM nginx:alpine

# Copy the built site to nginx
COPY --from=builder /app/site/.kobweb/site /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port
EXPOSE 8080
