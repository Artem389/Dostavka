FROM maven:3.9.6-openjdk-17 AS build
WORKDIR /app
COPY ../pom.xml /app/pom.xml
COPY . /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
