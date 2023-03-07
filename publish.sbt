ThisBuild / organization := "com.cyrildever"
ThisBuild / organizationName := "Cyril Dever"
ThisBuild / organizationHomepage := Some(url("http://www.cyrildever.com/"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/cyrildever/feistel-jar"),
    "scm:git@github.com:cyrildever/feistel-jar.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "cyrildever",
    name = "Cyril Dever",
    email = "technique@cyrildever.com",
    url = url("https://github.com/cyrildever")
  )
)

ThisBuild / description := "Library implementing the Feistel cipher for Format-Preserving Encryption (FPE)."
ThisBuild / licenses := List(
  "MIT License" -> new URL("https://opensource.org/licenses/MIT")
)
ThisBuild / homepage := Some(url("https://github.com/cyrildever/feistel-jar"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true
