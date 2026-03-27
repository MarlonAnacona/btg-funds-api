# -------- BUILD --------
FROM gradle:8.14.4-jdk17 AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

RUN chmod +x gradlew
RUN ./gradlew build -x test

# -------- RUNTIME --------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
ARG SPRING_DATA_MONGODB_URI
ARG SPRING_MAIL_USERNAME
ARG SPRING_MAIL_PASSWORD
ARG SERVER_PORT

ENV SPRING_DATA_MONGODB_URI=$SPRING_DATA_MONGODB_URI
ENV SPRING_MAIL_USERNAME=$SPRING_MAIL_USERNAME
ENV SPRING_MAIL_PASSWORD=$SPRING_MAIL_PASSWORD
ENV SERVER_PORT=$SERVER_PORT

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]