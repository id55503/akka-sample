name := "akka-sample"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaVersion = "2.4.12"
  Seq("com.typesafe.akka" % "akka-stream_2.11" % akkaVersion)
}