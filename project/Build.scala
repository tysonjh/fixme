import sbt._
import Keys._

object BuildSettings {
  val paradiseVersion = "2.0.0-M8"
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.tysonjh",
    version := "0.1-SNAPSHOT",
    scalacOptions ++= Seq(),
    scalaVersion := "2.11.0-RC4",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.sonatypeRepo("releases"),
    autoCompilerPlugins := true
  ) 
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings ++ Seq(
      run <<= run in Compile in core
    )
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies ++= (
        if (scalaVersion.value.startsWith("2.10")) 
          List(
            "org.scalamacros" %% "quasiquotes" % paradiseVersion,
            compilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)
          )
        else Nil
      ),
      initialCommands in console := """
        |import reflect.runtime.universe
        |import universe._
        |import reflect.runtime.currentMirror
        |import tools.reflect.ToolBox
        |val toolbox = currentMirror.mkToolBox()
      """.stripMargin
    )
  )

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = buildSettings
  ) dependsOn(macros)
}
