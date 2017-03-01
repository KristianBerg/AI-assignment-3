name := "RobotHMM"

version := "0.1"

scalaVersion := "2.11.8"

javaSource in Compile := baseDirectory.value / "src"
scalaSource in Compile := baseDirectory.value / "src"

libraryDependencies  ++= Seq(
  "org.scalanlp" %% "breeze" % "0.13",
  "org.scalanlp" %% "breeze-natives" % "0.13",
  "org.scalanlp" %% "breeze-viz" % "0.13",
  "org.scalaz" %% "scalaz-core" % "7.2.9",
  "com.chuusai" %% "shapeless" % "2.3.2"
)

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")
