#!/usr/bin/env bash

cd kafka-consumer
sbt assembly
pwd
java -jar target/scala-2.12/kafka-consumer-assembly-0.1.jar