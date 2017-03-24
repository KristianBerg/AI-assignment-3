name := "RobotHMM"

version := "0.1"

scalaVersion := "2.11.8"

javaSource in Compile := baseDirectory.value / "src"
scalaSource in Compile := baseDirectory.value / "src"

mainClass in (Compile, run) := Some("control.Main")

libraryDependencies  ++= Seq(
  "org.scalanlp" %% "breeze" % "0.13",
  "org.scalanlp" %% "breeze-natives" % "0.13",
  "org.scalanlp" %% "breeze-viz" % "0.13"
)

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")
