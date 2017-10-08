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
  "com.typesafe.akka" %% "akka-testkit" % Versions.akka % Test,
  "com.typesafe.akka" %% "akka-http" % Versions.akka_http,
//  "com.typesafe.akka" %% "akka-http-testkit" % Versions.akka_http % Test,
  "org.jsoup" % "jsoup" % "1.10.3",

  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.1-akka-2.5.x",
  "com.typesafe" % "config" % "1.2.1",

  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.json4s" %% "json4s-native" % "3.5.3",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
dependencyOverrides ++= Set(
  // akka-http bug : akka-stream 2.4.9 version을 가져옴
  "com.typesafe.akka" %%  "akka-stream" % Versions.akka
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
