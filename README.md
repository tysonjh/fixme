# FIXME

Generate compiler warnings with FIXMEs and TODOs. FIXMEs are used to identify a known bug or issue in code that requires a solution. TODOs are used to identify improvement areas such as optimizations or refactorings. The syntax to use FIXME and TODO is the same, so the examples below work for both. Also don't worry about the overhead incurred using FIXME, it operates at compile time so there are no runtime costs!

There are two ways to use FIXME, 

* as an expression within the body of a class/object/method
* as an annotation

```scala
import fixme._ // need this import!

FIXME("2013/04/10: This expression will abort compilation if not fixed by 2013/04/10")

FIXME("This expression always generates a warning")

@FIXME("2013/04/10: This annotation will abort compilation if not fixed by 2013/04/10")

@FIXME("This annotation always generates a warning")
```

For example,

```scala
[error] /Users/tysonjh/Development/fixme/core/src/main/scala/Test.scala:1: FIXME DATE PASSED (2013/04/10): This annotation will abort compilation if not fixed by 2013/04/10
[error] @FIXME("2013/04/10: This annotation will abort compilation if not fixed by 2013/04/10")
[error]  ^
[warn] /Users/tysonjh/Development/fixme/core/src/main/scala/Test.scala:4: FIXME: This annotation always generates a warning
[warn]   @FIXME("This annotation always generates a warning")
[warn]    ^
[error] /Users/tysonjh/Development/fixme/core/src/main/scala/Test.scala:6: FIXME DATE PASSED (2013/04/10): This expression will abort compilation if not fixed by 2013/04/10
[error]     FIXME("2013/04/10: This expression will abort compilation if not fixed by 2013/04/10")
[error]          ^
[warn] /Users/tysonjh/Development/fixme/core/src/main/scala/Test.scala:10: FIXME: This expression always generates a warning
[warn]   FIXME("This expression always generates a warning")
[warn]        ^
[warn] two warnings found
[error] two errors found
[error] (core/compile:compile) Compilation failed
[error] Total time: 2 s, completed Apr 22, 2014 11:36:52 AM
```

# Why?

FIXMEs and TODOs show up often in software projects with a developer's best intentions of returning to resolve them. Very often they are forgotten, pushed aside, ignored or never seen at all. This library makes sure they're noticed, by constantly nagging with compiler warnings or completely refusing to compile with errors.

# Using with SBT

### As a SBT plugin (recommended)

In the your `project/plugins.sbt` add

```scala
resolvers += "tysonjh releases" at "http://tysonjh.github.io/releases/"

addSbtPlugin("com.tysonjh" % "sbt-fixme" % "1.4")
```

and in your `build.sbt` add

```scala
fixmeSettings
```

### As a dependency

In your SBT build settings add,

```scala
resolvers += "tysonjh releases" at "http://tysonjh.github.io/releases/"

libraryDependencies <++= (scalaVersion) { v: String ⇒
  (if (v.startsWith("2.10")) List("org.scalamacros" %% "quasiquotes" % "2.0.0" % "compile")
  else Nil) :+
  "org.scala-lang" % "scala-reflect" % v % "compile" :+
  "com.tysonjh" %% "fixme" % "1.4" % "compile"
}

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full)
```

The above complexity can be reduced if using Scala 2.11.x since `quasiquotes` are part of the standard library. The compiler plugin is required to gain access to macro annotations, it can also be removed if FIXME is to be used as an expression only. _BEWARE_ removing the macro paradise plugin could be confusing since the annotation is still present but not functional!