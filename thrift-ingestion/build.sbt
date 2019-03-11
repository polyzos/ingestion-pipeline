ThisBuild / organization := "com.ipolyzos"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version      := "0.1"

lazy val dependencies =
  new {
    val thriftV         = "0.12.0"
    val logbackV        = "1.2.3"
    val scalaLoggingV   = "3.9.2"
    val kafkaV          = "1.1.0"
    val typesafeconfigV = "1.3.2"
    val phantomV        = "2.37.0"
    val akkaV           = "2.5.21"

    val thrift          = "org.apache.thrift"           % "libthrift"       % thriftV
    val logback         = "ch.qos.logback"              % "logback-classic" % logbackV
    val scalaLogging    = "com.typesafe.scala-logging"  %% "scala-logging"  % scalaLoggingV
    val kafka           = "org.apache.kafka"            %% "kafka"          % kafkaV
    val typesafeConfig  = "com.typesafe"                % "config"          % typesafeconfigV
  }

lazy val commonDependencies = Seq(
  dependencies.thrift,
  dependencies.logback,
  dependencies.scalaLogging,
  dependencies.kafka,
  dependencies.typesafeConfig
)

lazy val commons = (project in file("thrift-commons"))
  .settings(libraryDependencies ++= commonDependencies)

lazy val thriftClient = (project in file("thrift-client"))
  .settings(libraryDependencies ++= commonDependencies)
  .dependsOn(commons)

lazy val thriftServer = (project in file("thrift-server"))
  .settings(libraryDependencies ++= commonDependencies)
  .dependsOn(commons)
