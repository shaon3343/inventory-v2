name := """inventory.v2"""
organization := "com.shaon"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean,PlayEnhancer)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.apache.poi" % "poi" % "3.10-FINAL",
  "org.apache.poi" % "poi-ooxml" % "3.9",
  "dom4j" % "dom4j" % "1.6.1",
  "org.apache.xmlbeans" % "xmlbeans" % "2.3.0",
  guice,
  javaCore,
  javaJdbc,
  "mysql" % "mysql-connector-java" % "5.1.22",
  "com.typesafe.play" %% "play-ebean" % "4.1.0",
  evolutions,
  "org.avaje.ebeanorm" % "avaje-ebeanorm-api" % "3.1.1",
  "org.avaje.ebean" % "ebean" % "9.3.1"
)
