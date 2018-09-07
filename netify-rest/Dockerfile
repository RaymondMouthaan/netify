# Start with a base image containing Java runtime
#FROM openjdk:8-jdk-alpine
FROM openjdk:8-jre-alpine

# Add Maintainer Info
LABEL maintainer="raymondmmouthaan@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE
ADD target/${JAR_FILE} ${JAR_FILE}

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/netify-rest.jar"]