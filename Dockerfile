FROM openjdk:17-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ./target/backend-oficial-0.0.9.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]