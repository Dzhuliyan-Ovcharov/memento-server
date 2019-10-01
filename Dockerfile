FROM openjdk:12
MAINTAINER djuliqn.bg@gmail.com
ADD web/target/memento.jar memento.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "memento.jar"]