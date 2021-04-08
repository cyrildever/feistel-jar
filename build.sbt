organization := "fr.edgewhere"
name := "feistel-jar"
version := "1.3.1"
scalaVersion := "2.12.13"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
mainClass in assembly := Some("fr.edgewhere.feistel.Main")
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.0.0-RC2",
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.scorexfoundation" %% "scrypto" % "2.1.6",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.68" % "provided"
)
