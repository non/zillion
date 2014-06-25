name := "zillion"

organization := "org.spire-math"

version := "0.1.0"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4", "2.11.1")

libraryDependencies ++= Seq(
  "org.spire-math" %% "spire" % "0.7.4",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-feature"
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("http://github.com/non/zillion"))

seq(bintrayResolverSettings: _*)

seq(bintrayPublishSettings: _*)
