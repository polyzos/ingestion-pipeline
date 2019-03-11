name := "kafka-consumer"

version := "0.1"

scalaVersion := "2.12.8"

val akkaV           = "2.5.21"
val thriftV         = "0.12.0"
val phantomV        = "2.37.0"
val typesafeconfigV = "1.3.2"
val logbackV        = "1.2.3"
val scalaLoggingV   = "3.9.2"

libraryDependencies ++= Seq(
  "com.outworkers"    %% "phantom-dsl"  % phantomV,
  "com.typesafe.akka" %% "akka-actor"   % akkaV,
  "org.apache.thrift"  % "libthrift"    % thriftV,
  "com.typesafe"       % "config"       % typesafeconfigV,
  "ch.qos.logback"              % "logback-classic" % logbackV,
  "com.typesafe.scala-logging"  %% "scala-logging"  % scalaLoggingV
)

assemblyMergeStrategy in assembly := {
  case "META-INF/io.netty.versions.properties" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}