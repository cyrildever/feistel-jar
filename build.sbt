organization := "com.cyrildever"
name := "feistel-jar"
version := "1.5.6"
scalaVersion := "2.12.13"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
mainClass in assembly := Some("com.cyrildever.feistel.Main")
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.1.0",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test",
  "org.scorexfoundation" %% "scrypto" % "2.3.0",
  "org.bouncycastle" % "bcprov-jdk15to18" % "1.78.1" % "provided"
)
