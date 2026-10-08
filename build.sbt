ThisBuild / organization := "com.metro"
ThisBuild / scalaVersion := "2.13.14"

name := "metro-ufm-demo"

Compile / mainClass := Some("com.metro.ufm.MetroUfmServer")

libraryDependencies ++= Seq(
  "org.apache.wicket" % "wicket-core" % "9.18.0",
  "org.eclipse.jetty" % "jetty-server" % "9.4.54.v20240208",
  "org.eclipse.jetty" % "jetty-servlet" % "9.4.54.v20240208",
  "javax.servlet" % "javax.servlet-api" % "4.0.1" % Provided,
  "org.slf4j" % "slf4j-simple" % "2.0.12",
  "com.oracle.database.jdbc" % "ojdbc11" % "23.6.0.24.10",
  "com.github.librepdf" % "openpdf" % "1.3.39",
  "org.scalatest" %% "scalatest" % "3.2.18" % Test
)

assembly / assemblyMergeStrategy := {
  case "module-info.class" =>
    MergeStrategy.discard

  case PathList("META-INF", "versions", _, "module-info.class") =>
    MergeStrategy.discard

  case path =>
    MergeStrategy.defaultMergeStrategy(path)
}