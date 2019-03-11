FROM openjdk:8-jre-alpine

COPY target/scala-2.12/thriftServer-assembly-0.1.jar /thrift-server.jar

EXPOSE 9090

CMD ["/usr/bin/java", "-jar", "/thrift-server.jar"]