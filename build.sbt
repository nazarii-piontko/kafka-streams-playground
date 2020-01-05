name := "KafkaStreamsPlayground"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.4.0"

val logback = "1.2.3"
libraryDependencies += "ch.qos.logback" % "logback-core" % logback
libraryDependencies += "ch.qos.logback" % "logback-classic" % logback

libraryDependencies += "com.nrinaudo" %% "kantan.csv" % "0.6.0"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.6"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.apache.kafka" % "kafka-streams-test-utils" % "2.4.0" % "test"
)
