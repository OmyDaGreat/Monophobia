# Build stage
FROM eclipse-temurin:21-jdk as builder

# Set working directory
WORKDIR /app

# Set Kobweb version
ENV KOBWEB_CLI_VERSION=0.9.18

# Install required packages including Playwright dependencies
RUN apt-get update && apt-get install -y \
    unzip \
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
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

# Download and install Playwright browser
RUN curl -L -o playwright.deb https://github.com/microsoft/playwright/releases/download/v1.41.2/ms-playwright-focal_1.41.2-1_amd64.deb \
    && apt-get update \
    && apt-get install -y ./playwright.deb \
    && rm playwright.deb \
    && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY . .

# TODO: RUN PRODUCTION INSTEAD OF EXPORTING

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
