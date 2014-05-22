import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

object BuildSettings {
  lazy val paradiseVersion = "2.0.0"

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.tysonjh",
    version := "1.4",
    scalacOptions ++= Seq("-unchecked"),
    scalaVersion := "2.10.4") 

  lazy val noPublish = Seq(publishArtifact := false, publish := {}, publishLocal := {})

  lazy val withPublish = Seq(publishTo <<= version { ver: String ⇒
    if (ver.trim.endsWith("-SNAPSHOT")) Some(Resolver.file("Snapshots", file("/repository/snapshots/")))
    else Some(Resolver.file("Releases", file("/repository/releases/")))
  })
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings ++ noPublish ++ Seq(
      run <<= run in Compile in examples)
  ).aggregate(macros, plugin, examples)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ 
      withPublish ++ 
      Seq(
        name := "fixme",
        crossScalaVersions := Seq("2.10.2", "2.10.3", "2.11.0", "2.11.1"),
        libraryDependencies <++= (scalaVersion){ v: String ⇒
          (if (v.startsWith("2.10")) List("org.scalamacros" %% "quasiquotes" % paradiseVersion)
          else Nil) :+ "org.scala-lang" % "scala-reflect" % v % "compile"
        },
        addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full),
        initialCommands in console := """
          |import reflect.runtime.universe
          |import universe._
          |import reflect.runtime.currentMirror
          |import tools.reflect.ToolBox
          |val toolbox = currentMirror.mkToolBox()
        """.stripMargin
      )
  )

  lazy val plugin: Project = Project(
    "plugin",
    file("plugin"),
    settings = buildSettings ++ 
      withPublish ++ 
      Seq(
        name := "sbt-fixme",
        sbtPlugin := true
      )
  ).dependsOn(macros) 

  lazy val examples: Project = Project(
    "examples",
    file("examples"),
    settings = buildSettings ++ 
      noPublish ++ 
      Seq(
        libraryDependencies += "org.scalamacros" %% "quasiquotes" % paradiseVersion % "compile",
        addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full))
  ).dependsOn(macros)
}
