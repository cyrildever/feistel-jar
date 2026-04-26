organization := "com.cyrildever"
name := "feistel-jar"
version := "1.5.9"
scalaVersion := "2.12.13"

// Sonatype settings
sonatypeProfileName := "com.cyrildever"
sonatypeCredentialHost := "central.sonatype.com"

// Credentials
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")

// Publishing
Compile / packageDoc / publishArtifact := true
Compile / packageSrc / publishArtifact := true
Test / publishArtifact := false

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
  "org.bouncycastle" % "bcprov-jdk15to18" % "1.84" % "provided"
)

ThisBuild / useGpg := true
ThisBuild / pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toArray)
ThisBuild / PgpKeys.useGpg := true
