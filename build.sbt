name := "KafkaStreamsPlayground"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.4.0"

val logback = "1.2.3"
libraryDependencies += "ch.qos.logback" % "logback-core" % logback
libraryDependencies += "ch.qos.logback" % "logback-classic" % logback

libraryDependencies += "org.apache.kafka" % "kafka-streams-test-utils" % "2.4.0" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"
