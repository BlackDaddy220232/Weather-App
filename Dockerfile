FROM openjdk:17

WORKDIR /app

COPY target/Weather-0.0.1-SNAPSHOT.jar weather.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "weather.jar"]