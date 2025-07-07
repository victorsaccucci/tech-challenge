FROM maven:3.9.10-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests





FROM eclipse-temurin:21.0.7_6-jre-ubi9-minimal

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]