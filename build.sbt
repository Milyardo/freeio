name := "freeio"

description := "FreeIO implements an AST for performing file IO operations ina genral way."

scalaVersion := "2.12.1"

scalacOptions ++= Seq(
  "-Xlint"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.10",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.10"
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
  "org.scalatest"  %% "scalatest" % "3.0.1" % "test"
)
