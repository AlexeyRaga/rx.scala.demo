
scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.10" % "2.0.M6" % "test"
)

transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)

