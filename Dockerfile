#
# Build stage
#
FROM maven:3.9.1-eclipse-temurin-17 AS build
# Setup app
RUN mkdir -p /app

# Switch working environment
WORKDIR /app

# Add application
COPY ./ .

RUN mvn -f /app/pom.xml -DskipTests clean package
RUN mvn -f /app/pom.xml flyway:migrate

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /app/target/socialbotnet-4.2-jar-with-dependencies.jar /usr/local/lib/socialbotnet.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/socialbotnet.jar"]