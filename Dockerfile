# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim as builder

ARG VERSION
ENV VERSION=$VERSION

# Copy the Maven build artifact (JAR file) from the local machine
RUN mkdir "/opt/treasure"

COPY target/opposite-treasure-${VERSION}.jar /opt/treasure/opposite-treasure-${VERSION}.jar

# Создаем скрипт start-up.sh непосредственно в контейнере
RUN echo '#!/bin/bash\njava -jar /opt/treasure/opposite-treasure-${VERSION}.jar' > /opt/treasure/start-up.sh

# Даем права на выполнение для скрипта
RUN chmod +x /opt/treasure/start-up.sh

# 1. Установим системные сертификаты (чтобы работал Let's Encrypt и другие публичные CA)
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# 2. Скопируем твой кастомный сертификат внутрь контейнера
COPY selfsigned.crt /usr/local/share/ca-certificates/ca.crt

# 3. Обновим системные сертификаты
RUN update-ca-certificates

# 4. Добавим сертификат в truststore Java
RUN keytool -importcert -noprompt -trustcacerts \
    -alias myca \
    -file /usr/local/share/ca-certificates/ca.crt \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["/opt/treasure/start-up.sh"]