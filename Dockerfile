FROM openjdk:11
COPY target/*.jar spring-boot-app.jar
EXPOSE 8080
ENTRYPOINT java -jar spring-boot-app.jar