FROM coolcjg/encoder:1.0
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 5000
ENTRYPOINT ["/java/bin/java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]