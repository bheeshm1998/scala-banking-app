name := """TransactionService"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test


libraryDependencies ++= Seq(
  guice,
  "org.playframework" %% "play-slick"            % "6.1.0",
  "org.playframework" %% "play-slick-evolutions" % "6.1.0",
  "mysql" % "mysql-connector-java" % "8.0.26", // my-sql connector dependency
)

//libraryDependencies ++= Seq(
//  "com.typesafe.play" %% "play-ws" % "2.8.1"
//)
//
//dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"

//libraryDependencies += "com.typesafe.play" %% "play-ws" % play.core.PlayVersion.current

libraryDependencies += ws
lazy val akkaVersion = sys.props.getOrElse("akka.version", "2.9.3")
//libraryDependencies ++= Seq(
//  "org.apache.kafka" %% "kafka" % "3.7.0",
////  "org.apache.kafka" %% "kafka-clients" % "2.8.0",
//  "com.typesafe.akka" %% "akka-stream-kafka" % "6.0.0",
//  "com.typesafe.akka" %% "akka-stream" % akkaVersion
//)

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "2.8.0",
  "org.apache.kafka" % "kafka-clients" % "2.8.0",
  "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.7",
  "com.typesafe.akka" %% "akka-stream" % "2.6.14"
)



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
