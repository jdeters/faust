ThisBuild / scalaVersion     := "2.13.7"
ThisBuild / version          := "1.0-SNAPSHOT"
ThisBuild / organization     := "edu.wustl.sbs"

lazy val root = (project in file("."))
  .settings(
    name := "faust",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "scalameta" % "4.6.0",
      "org.scala-graph" %% "graph-core" % "1.13.2",
      "org.scala-graph" %% "graph-constrained" % "1.13.2",
      "org.scala-graph" %% "graph-json" % "1.13.0",
      "org.scala-graph" %% "graph-dot" % "1.13.0"
    ),
    scalacOptions ++= Seq(
     "-language:postfixOps",
    )
  )