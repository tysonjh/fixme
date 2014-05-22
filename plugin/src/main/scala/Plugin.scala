package fixme

import sbt._
import Keys._

object Plugin extends sbt.Plugin {
  val paradiseVersion = "2.0.0"
  val fixmeVersion = "1.4"
  val fixmeSettings: Seq[Setting[_]] = Seq(
    resolvers += "tysonjh releases" at "http://tysonjh.github.io/releases/",
    libraryDependencies <++= (scalaVersion) { v: String ⇒
      (if (v.startsWith("2.10")) List("org.scalamacros" %% "quasiquotes" % paradiseVersion % "compile")
      else Nil) :+
        "org.scala-lang" % "scala-reflect" % v % "compile" :+
        "com.tysonjh" %% "fixme" % fixmeVersion % "compile"
    },
    addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full))
}