# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim as builder

ARG VERSION
ENV VERSION=$VERSION

# Copy the Maven build artifact (JAR file) from the local machine
RUN mkdir "/opt/treasure"

COPY target/opposite-treasure-${VERSION}.jar /opt/treasure/opposite-treasure-${VERSION}.jar

# Создаем скрипт start-up.sh непосредственно в контейнере
RUN echo '#!/bin/bash\njava -jar /opt/treasure/opposite-treasure-${VERSION}.jar' > /opt/treasure/start-up.sh

RUN cat /opt/treasure/start-up.sh

# Даем права на выполнение для скрипта
RUN chmod +x /opt/treasure/start-up.sh


RUN ls -l /opt/treasure

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["/opt/treasure/start-up.sh"]