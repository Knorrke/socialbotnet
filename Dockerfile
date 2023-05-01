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

RUN mvn -DskipTests clean package
#RUN mvn flyway:migrate


#ADD JDBC_DATABASE_URL if only DB_HOST, DB_PORT and DB_DATABASE is set.
ENV JDBC_DATABASE_URL=${JDBC_DATABASE_URL:-"jdbc:postgres://${DB_HOST}:${DB_PORT}/${DB_DATABASE}"}

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /app/target/socialbotnet-4.2-jar-with-dependencies.jar /usr/local/lib/socialbotnet.jar
EXPOSE 30003
ENTRYPOINT ["java", "-jar", "/usr/local/lib/socialbotnet.jar"]