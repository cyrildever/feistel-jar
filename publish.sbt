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
ThisBuild / licenses := List("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / homepage := Some(url("https://github.com/cyrildever/feistel-jar"))

ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / publishMavenStyle := true
