name := "funfunnet-crawler"

version := "1.0"
scalaVersion := Versions.scala
organization := "net.funfunnet.crawler"
javacOptions := Seq("-source", "1.8", "-target", "1.8")
scalacOptions := Seq("-target:jvm-1.8")

// scala-config
Seq(unmanagedResourceDirectories in Compile += baseDirectory.value / "conf")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % Versions.akka,
  "com.typesafe.akka" %% "akka-testkit" % Versions.akka,
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.1-akka-2.5.x",
  "com.typesafe" % "config" % "1.2.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

// scalastyle configurations
// test task 수행시, scalastyle에 실패하면 에러를 발생시킴
scalastyleFailOnError := true

// check scalastyle in compile time
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
(compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value

// assembly
assemblyJarName in assembly := s"${name.value}-assembly.jar"
mainClass in assembly := Some("net.funfunnet.crawler.Application")
test in assembly := {}
