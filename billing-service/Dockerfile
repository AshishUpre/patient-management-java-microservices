FROM docker.io/library/maven:3.9.9-eclipse-temurin-21 as builder
# as we are using podman and for it docker is one of the repos, state that we want to use docker.io to avoid ambiguity

WORKDIR /app
COPY pom.xml .

# cache the dependencies unless changes detected
RUN mvn dependency:go-offline -B
Copy src ./src
RUN mvn clean package


# same reason as above -- specify to get from docker.io
FROM docker.io/library/openjdk:21-jdk AS runner

WORKDIR /app
COPY --from=builder /app/target/billing-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 4001
# grpc server port
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]

