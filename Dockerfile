FROM openjdk:8-jre-stretch
ARG JAR_FILE
ARG SCHEMA_FILE
ADD ${JAR_FILE} app.jar
ADD ${SCHEMA_FILE} schema.zip
