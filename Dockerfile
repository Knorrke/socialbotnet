#
# Build stage
#
FROM maven:3.9.1-eclipse-temurin-17 AS build
ARG JDBC_DATABASE_USERNAME
ARG JDBC_DATABASE_PASSWORD
ARG DB_HOST
ARG DB_PORT
ARG DB_DATABASE

#ADD JDBC_DATABASE_URL from DB_HOST, DB_PORT and DB_DATABASE because render.com can't generate JDBC connect string
ENV JDBC_DATABASE_URL="jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_DATABASE?user=$JDBC_DATABASE_USERNAME&password=$JDBC_DATABASE_PASSWORD&sslmode=require"

# Setup app
RUN mkdir -p /app

# Switch working environment
WORKDIR /app

# Add application
COPY ./ .

RUN mvn -DskipTests clean package
RUN mvn flyway:migrate

#
# Package stage
#
FROM openjdk:17
ARG JDBC_DATABASE_USERNAME
ARG JDBC_DATABASE_PASSWORD
ARG DB_HOST
ARG DB_PORT
ARG DB_DATABASE
COPY --from=build /app/target/socialbotnet-4.2-jar-with-dependencies.jar /usr/local/lib/socialbotnet.jar
#ADD JDBC_DATABASE_URL from DB_HOST, DB_PORT and DB_DATABASE because render.com can't generate JDBC connect string
ENV JDBC_DATABASE_URL="jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_DATABASE?user=$JDBC_DATABASE_USERNAME&password=$JDBC_DATABASE_PASSWORD&sslmode=require"
EXPOSE 30003
ENTRYPOINT ["java", "-jar", "/usr/local/lib/socialbotnet.jar"]