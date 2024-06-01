import scala.collection.immutable.Seq

name := """NotificationService"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "2.8.0",
  "org.apache.kafka" % "kafka-clients" % "2.8.0",
  "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.7",
  "com.typesafe.akka" %% "akka-stream" % "2.6.14"
)


libraryDependencies += "com.typesafe.play" %% "filters-helpers" % "2.8.8"

dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "2.2.0"
