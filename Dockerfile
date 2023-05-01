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
ENV URL_FROM_RENDER=${SET_FROM_RENDERCOM:+"jdbc:postgres://${DB_HOST}:${DB_PORT}/${DB_DATABASE}?user=${JDBC_DATABASE_USERNAME}&password=user=${JDBC_DATABASE_PASSWORD}&sslmode=require"}
ENV JDBC_DATABASE_URL=${URL_FROM_RENDER:-${JDBC_DATABASE_URL}}

#
# Package stage
#
FROM openjdk:17
COPY --from=build /app/target/socialbotnet-4.2-jar-with-dependencies.jar /usr/local/lib/socialbotnet.jar
EXPOSE 30003
ENTRYPOINT ["java", "-jar", "/usr/local/lib/socialbotnet.jar"]