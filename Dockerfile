FROM coolcjg/encoder:1.0
COPY encoder.jar encoder.jar
EXPOSE 5000
ENTRYPOINT ["/java/bin/java","-jar", "-Dspring.profiles.active=prod", "encoder.jar"]