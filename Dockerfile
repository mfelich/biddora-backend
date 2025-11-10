FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
ADD target/biddora-backend.jar biddora-backend.jar
ENTRYPOINT ["java", "-jar", "biddora-backend.jar"]