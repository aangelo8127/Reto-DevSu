FROM openjdk:11-jre-slim

WORKDIR /app

COPY cliente-persona-service/target/cliente-persona-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
