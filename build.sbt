organization := "com.cyrildever"
name := "feistel-jar"
version := "1.5.8"
scalaVersion := "2.12.13"

// Sonatype settings
sonatypeProfileName := "com.cyrildever"
sonatypeCredentialHost := "central.sonatype.com"

// Credentials
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")

// Publishing
publishArtifact in (Compile, packageDoc) := true
publishArtifact in (Compile, packageSrc) := true

// Assembly settings (keep your existing ones)
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
mainClass in assembly := Some("com.cyrildever.feistel.Main")
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.1.0",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test",
  "org.scorexfoundation" %% "scrypto" % "3.0.0",
  "org.bouncycastle" % "bcprov-jdk15to18" % "1.83" % "provided"
)