organization := "com.micronautics"

name := "pratik_kshirsagar_hw2"

version := "0.1.0"

scalaVersion := "2.12.3"

autoScalaLibrary := false

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-target:jvm-1.8",
  "-unchecked",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Xlint"
)

scalacOptions in (Compile, doc) ++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/{name.value}/tree/master€{FILE_PATH}.scala"
  )
}.value

javacOptions ++= Seq(
  "-Xlint:deprecation",
  "-Xlint:unchecked",
  "-source", "1.8",
  "-target", "1.8",
  "-g:vars"
)

resolvers ++= Seq(
)

libraryDependencies ++= Seq(
  "org.scalatest"     %% "scalatest"   % "3.0.3" % Test withSources(),
  "junit"             %  "junit"       % "4.12"  % Test
)

resourceDirectory in Compile := baseDirectory.value / "src/java/org/imgscalr"

resourceDirectory in Test := baseDirectory.value / "src/test/resources"


//javaSource in Compile := baseDirectory.value / "src/java/org/imgscalr"

//javaSource in Test := baseDirectory.value / "src/test/java/org/imgscalr"



logLevel := Level.Warn

//coverageEnabled := true

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
logLevel in test := Level.Info

// define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
initialCommands in console := """
                                |""".stripMargin

cancelable := true

//sublimeTransitive := true

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")


