name := "freeio"

description := "FreeIO implements an AST for performing file IO operations ina genral way."

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-Xlint",
  "-Ybackend:GenBCode",
  "-Xexperimental",
  "-Xfuture",
  "-optimise"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.1",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.1"
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.0" % "test",
  "org.scalatest"  %% "scalatest" % "2.2.6" % "test"
)
