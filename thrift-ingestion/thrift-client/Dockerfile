FROM openjdk:8-jre-alpine

COPY target/scala-2.12/thriftClient-assembly-0.1.jar /thrift-client.jar

CMD ["/usr/bin/java", "-jar", "/thrift-client.jar"]