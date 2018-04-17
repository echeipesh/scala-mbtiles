name := "doobie-mbtiles"

version := "0.0.1"

description := "Read MBTiles database using doobie"

organization := "com.azavea"

organizationName := "Azavea"

scalaVersion in ThisBuild := Version.scala

val common = Seq(
  resolvers ++= Seq(),

  scalacOptions := Seq(
    "-deprecation",
    "-Ypartial-unification",
    "-Ywarn-value-discard",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen"
  ),

  scalacOptions in (Compile, doc) += "-groups",

  libraryDependencies ++= Seq(
    "org.tpolecat"                %% "doobie-core"           % "0.5.2",
    "org.xerial"                  %  "sqlite-jdbc"           % "3.21.0",
    "org.scalatest"               %% "scalatest"             % "3.0.1" % Test,
    "org.typelevel"               %% "cats-core"             % "1.0.0-RC1",
    "com.monovore"                %% "decline"               % "0.4.0-RC1",
    "com.typesafe.akka" %% "akka-actor"  % "2.4.3",
    "com.typesafe.akka" %% "akka-http" % "10.0.3",
    "ch.megard" %% "akka-http-cors" % "0.2.2"
  ),

  parallelExecution in Test := false
)

fork in console := true

val release = Seq(
  licenses += ("Apache-2.0", url("http://apache.org/licenses/LICENSE-2.0"))
)

assemblyMergeStrategy in assembly := {
  case s if s.startsWith("META-INF/services") => MergeStrategy.concat
  case "reference.conf" | "application.conf"  => MergeStrategy.concat
  case "META-INF/MANIFEST.MF" | "META-INF\\MANIFEST.MF" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.RSA" | "META-INF/ECLIPSEF.SF" => MergeStrategy.discard
  case _ => MergeStrategy.first
}

lazy val root = Project("doobie-mbtiles", file(".")).
  settings(common, release).
  settings(
    initialCommands in console :=
      """
      |import cats.implicits._
      """.stripMargin
  )
