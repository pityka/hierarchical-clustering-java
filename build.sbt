

name := "hierarchical-clustering-fork"

organization := "io.github.pityka"

version := "1.0-5"

scalaVersion := "2.11.8"

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test"

crossPaths := false

autoScalaLibrary := false

pomExtra in Global := {
  <licenses>
  <license>
    <name>Apache-2.0</name>
    <url>https://opensource.org/licenses/Apache-2.0</url>
  </license>
</licenses>
  <url>https://github.com/pityka/hierarchical-clustering-java</url>
  <scm>
    <connection>scm:git:github.com/pityka/hierarchical-clustering-java</connection>
    <developerConnection>scm:git:git@github.com:pityka/hierarchical-clustering-java</developerConnection>
    <url>github.com/pityka/hierarchical-clustering-java</url>
  </scm>
  <developers>
  <developer>
    <id>lbehnke</id>
    <name>Lars Behnke</name>
    <url>https://github.com/lbehnke</url>
    <roles>
        <role>developer</role>
        <role>administrator</role>
    </roles>
</developer>
  <developer>
    <id>pityka</id>
    <name>Istvan Bartha</name>
    <url>https://github.com/pityka</url>
  </developer>
</developers>
}
