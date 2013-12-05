name := "Guice Provider"

organization := "com.guiceprovider"

version := "0.1"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.5",
  "com.google.inject" % "guice" % "3.0",
  "org.specs2" %% "specs2" % "2.3.4" % "test"
)
