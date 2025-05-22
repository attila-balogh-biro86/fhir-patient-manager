FROM eclipse-temurin:17.0.12_7-jre-alpine AS builder

ENV USERNAME="littlebig"
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app/fhir-patient-manager.jar
COPY target/*.jar /app/fhir-patient-manager.jar

RUN addgroup -g 1001 "$USERNAME" && \
    adduser -u 1001 -G "$USERNAME" -s /bin/sh -D "$USERNAME"

# Change ownership of the application directory to the new user and group
RUN chown -R ${USERNAME}:${USERNAME} /app

# Switch to the new user
USER ${USERNAME}

# Use ENTRYPOINT to set up the command with JAVA_OPTS as an argument
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/fhir-patient-manager.jar"]

# Optionally, expose a port if your application listens on a specific port
EXPOSE 8089