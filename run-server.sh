#!/usr/bin/env bash

cd thrift-ingestion
sbt assembly

fuser -k 9090/tcp

java -jar thrift-server/target/scala-2.12/thriftServer-assembly-0.1.jar