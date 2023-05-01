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


#ADD JDBC_DATABASE_URL from DB_HOST, DB_PORT and DB_DATABASE because render.com can't generate JDBC connect string
ENV JDBC_DATABASE_URL=${SET_FROM_RENDERCOM:-"jdbc:postgres://${DB_HOST}:${DB_PORT}/${DB_DATABASE}"}

#
# Package stage
#
FROM openjdk:17
COPY --from=build /app/target/socialbotnet-4.2-jar-with-dependencies.jar /usr/local/lib/socialbotnet.jar
EXPOSE 30003
ENTRYPOINT ["java", "-jar", "/usr/local/lib/socialbotnet.jar"]