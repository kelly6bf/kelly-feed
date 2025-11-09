FROM amazoncorretto:21

WORKDIR /app

ARG JAR_FILE=build/libs/kelly-feed.jar
COPY ${JAR_FILE} .

ENV PROFILE=pt

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "kelly-feed.jar"]
