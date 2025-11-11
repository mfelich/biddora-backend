FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/biddora-backend.jar biddora-backend.jar
ENTRYPOINT ["java", "-jar", "biddora-backend.jar"]