FROM openjdk:11-jre-slim

WORKDIR /app

COPY cuenta-movimientos-service/target/cuenta-movimientos-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
