FROM debian:stable-slim
USER root

# Copy the project code to app dir
COPY . /app

# Install wget and sudo
RUN apt-get update  \
    && apt-get install -y wget  \
    && apt-get install -y sudo  \
    && apt-get clean

# Install OpenJDK-21
RUN wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb \
    && sudo dpkg -i jdk-21_linux-x64_bin.deb

# Fix certificate issues
RUN apt-get update \
    && apt-get install ca-certificates-java \
    && apt-get clean \
    && update-ca-certificates -f

# Setup JAVA_HOME dynamically
RUN export JAVA_HOME=$(update-alternatives --query java | grep 'Value: ' | awk '{print $2}' | sed 's:/bin/java::') \
    && echo "JAVA_HOME is set to $JAVA_HOME"
ENV JAVA_HOME=$JAVA_HOME
RUN java -version

# Add Chrome (for export)
RUN apt-get update \
    && apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    --no-install-recommends \
    && curl -sSL https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb https://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update && apt-get install -y \
    google-chrome-stable \
    fontconfig \
    fonts-ipafont-gothic \
    fonts-wqy-zenhei \
    fonts-thai-tlwg \
    fonts-kacst \
    fonts-symbola \
    fonts-noto \
    fonts-freefont-ttf \
    --no-install-recommends

# Install kobweb
RUN apt-get update && apt-get install -y wget unzip

RUN wget https://github.com/varabyte/kobweb/releases/download/cli-v0.9.4/kobweb-0.9.4.zip \
    && unzip kobweb-0.9.4.zip \
    && rm -r kobweb-0.9.4.zip
ENV PATH="/kobweb-0.9.4/bin:${PATH}"

WORKDIR /app

RUN kobweb export --mode dumb

RUN export PORT=$(kobweb conf server.port)
EXPOSE $PORT

# Purge all the things we don't need anymore

RUN apt-get purge --auto-remove -y curl gnupg wget unzip \
    && rm -rf /var/lib/apt/lists/*

# Keep container running because `kobweb run --mode dumb` doesn't block
CMD kobweb run --mode dumb --env prod && tail -f /dev/null
