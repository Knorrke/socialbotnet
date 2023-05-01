FROM openjdk:17-alpine

# Setup app
RUN mkdir -p /app

# Switch working environment
WORKDIR /app

# Add application
COPY ./ .

RUN chmod +x ./mvnw
RUN ./mvnw clean install
RUN ./mvnw flyway:migrate

# This is the port that your javalin application will listen on
EXPOSE 7000 
ENTRYPOINT ["java", "-jar", "./target/socialbotnet-4.2-jar-with-dependencies.jar"]