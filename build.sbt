name := "Spark Meetup"

version := "0.0.1"

scalaVersion := "2.11.6"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala
  
resolvers += Resolver.mavenLocal
resolvers += "spray repo" at "http://repo.spray.io"
  
libraryDependencies ++= Seq(
  "io.spray" %% "spray-can" % "1.3.3",
  "io.spray" %% "spray-json" % "1.3.2",
  "com.wandoulabs.akka" %% "spray-websocket" % "0.1.4",
  "org.apache.spark" %% "spark-core" % "1.4.1" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-streaming" % "1.4.1" withSources() withJavadoc(),
  "com.datastax.spark" %% "spark-cassandra-connector" % "1.4.0-M2" withSources() withJavadoc(),
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.7.1" withSources() withJavadoc(),
  "org.scalatest" %% "scalatest" % "2.2.4" % "test" withSources() withJavadoc()
)