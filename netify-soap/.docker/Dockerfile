# Start with a base image containing Java runtime
FROM amd64/openjdk:11-jre-slim
#FROM openjdk:8-jdk-alpine
#FROM openjdk:10-jre

# Add Maintainer Info
LABEL maintainer="raymondmmouthaan@gmail.com"

ENV SPRING_PROFILES_ACTIVE dev

# Make port 8080 available to the world outside this container
EXPOSE 8081

# The application's jar file
ARG JAR_FILE
ADD target/${JAR_FILE} ${JAR_FILE}

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/netify-soap.jar"]