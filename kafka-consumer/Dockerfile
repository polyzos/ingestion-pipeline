FROM openjdk:8-jre-alpine

COPY target/scala-2.12/kafka-consumer-assembly-0.1.jar /kafka-consumer.jar

EXPOSE 9090

CMD ["/usr/bin/java", "-jar", "/kafka-consumer.jar"]