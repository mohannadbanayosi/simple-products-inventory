name := """simple-products-inventory"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "com.h2database" % "h2" % "1.4.190",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc4",
  specs2 % Test
)

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test

fork in run := true