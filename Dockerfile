FROM openjdk:18

ARG FILE_JAR=target/eScentedCandle-0.0.1-SNAPSHOT.jar

ADD ${FILE_JAR} api-service.jar

ENTRYPOINT ["java", "-jar", "api-service.jar"]

EXPOSE 8081