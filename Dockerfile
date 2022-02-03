FROM openjdk:8-jdk-alpine as builder

# COPY gradlew .
# COPY gradle gradle
# COPY build.gradle .
# COPY settings.gradle .
# COPY src src
# RUN chmod +x ./gradlew
# RUN ./gradlew build
# RUN ./gradlew -Dflyway.configFiles=src/main/resources/flyway.conf flywayMigrate

EXPOSE 8080
ADD build/libs/tutorlab-0.0.1-SNAPSHOT.jar tutorlab-0.0.1-SNAPSHOT.jar
# ENTRYPOINT ["./gradlew", "flywayMigrate", "&&", "java", "-jar", "tutorlab-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT ["java", "-jar", "tutorlab-0.0.1-SNAPSHOT.jar"]