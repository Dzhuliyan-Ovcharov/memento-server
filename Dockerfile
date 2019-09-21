FROM openjdk:12
ADD web/target/memento.jar memento.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "memento.jar"]