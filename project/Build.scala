import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.tysonjh",
    version := "1.0",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation"),
    scalaVersion := "2.11.0") 

  val noPublish = Seq(publishArtifact := false, publish := {}, publishLocal := {})
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings ++ noPublish ++ Seq(
      run <<= run in Compile in core)
    ).aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      name := "fixme",
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
      initialCommands in console := """
        |import reflect.runtime.universe
        |import universe._
        |import reflect.runtime.currentMirror
        |import tools.reflect.ToolBox
        |val toolbox = currentMirror.mkToolBox()
      """.stripMargin,
      publishTo <<= version { (v: String) =>
        if (v.trim.endsWith("-SNAPSHOT"))
          Some(Resolver.file("Snapshots", file("/repository/snapshots/")))
        else
          Some(Resolver.file("Releases", file("/repository/releases/")))
      }
    )
  )

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = buildSettings ++ noPublish
  ).dependsOn(macros)
}
