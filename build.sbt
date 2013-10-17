
scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
    "com.netflix.rxjava" % "rxjava-scala" % "0.14.5",
    "com.netflix.rxjava" % "rxjava-apache-http" % "0.14.5",
    "org.scalatest" % "scalatest_2.10" % "2.0.RC2" % "test"
)

transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)
