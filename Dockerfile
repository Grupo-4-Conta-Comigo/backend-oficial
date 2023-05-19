FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/backend-oficial-0.0.9.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]