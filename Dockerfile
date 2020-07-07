FROM openjdk:12
COPY web/target/memento-executable.jar memento-executable.jar
COPY ./wait-for-it.sh wait-for-it.sh
EXPOSE 8081
ENTRYPOINT ["/bin/sh", "-c", "./wait-for-it.sh memento-db:3306 --timeout=500 --strict -- java -jar /memento-executable.jar"]
