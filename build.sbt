// previous version was "0.1.4"

import ReleaseTransformations._

lazy val zillionSettings = Seq(
  organization := "org.spire-math",
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  homepage := Some(url("http://github.com/non/zillion")),
  scalaVersion := "2.12.2",
  crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.2"),
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked"),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "spire" % "0.14.1",
    "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
    "org.scalacheck" %%% "scalacheck" % "1.13.5" % "test"
  ),
  scalaJSStage in Global := FastOptStage,
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <scm>
      <url>git@github.com:non/zillion.git</url>
      <connection>scm:git:git@github.com:non/zillion.git</connection>
    </scm>
    <developers>
      <developer>
        <id>non</id>
        <name>Erik Osheim</name>
        <url>http://github.com/non/</url>
      </developer>
    </developers>
  ),

  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
    pushChanges))

lazy val noPublish = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false)

lazy val root = project
  .in(file("."))
  .aggregate(zillionJS, zillionJVM)
  .settings(name := "zillion-root")
  .settings(zillionSettings: _*)
  .settings(noPublish: _*)

lazy val zillion = crossProject
  .crossType(CrossType.Pure)
  .in(file("."))
  .settings(name := "zillion")
  .settings(zillionSettings: _*)

lazy val zillionJVM = zillion.jvm

lazy val zillionJS = zillion.js
