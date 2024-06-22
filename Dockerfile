FROM openjdk:17-alpine AS builder

ARG JAR_FILE=build/libs/*.jar
ARG FFMPEG_PATH=build/resources/main/encoder/ffmpeg
ARG FFPROBE_PATH=build/resources/main/encoder/ffprobe

COPY ${JAR_FILE} app.jar
COPY ${FFMPEG_PATH} /encoder/ffmpeg
COPY ${FFPROBE_PATH} /encoder/ffprobe
EXPOSE 5000
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]