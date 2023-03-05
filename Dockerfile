FROM maven:3.8.6-openjdk-11 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn -e -B package

FROM openjdk:11-jre
COPY --from=builder /app/target/files-0.0.1-SNAPSHOT.jar /app/

EXPOSE 8080

CMD ["java", "-jar", "/app/files-0.0.1-SNAPSHOT.jar"]
