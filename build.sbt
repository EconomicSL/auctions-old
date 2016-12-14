lazy val commonSettings = Seq(
  scalaVersion := "2.12.1" ,
  name := "auctions",
  version := "0.1.0-SNAPSHOT",
  organization := "org.economicsl",
  organizationName := "EconomicSL",
  organizationHomepage := Some(url("https://economicsl.github.io/")),
  scalacOptions ++= Seq("-unchecked", "-deprecation")
)

lazy val Functional = config("functional") extend Test

lazy val core = (project in file(".")).
  settings(commonSettings: _*).
  configs(Functional).
  settings(inConfig(Functional)(Defaults.testSettings): _*).
  settings(
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1" % "functional, test",
      "org.apache.commons" % "commons-math3" % "3.6.1" % "functional, test",
      "org.scalatest" %% "scalatest" % "3.0.1" % "functional, test"
    ),
    parallelExecution in Functional := false
  )

    