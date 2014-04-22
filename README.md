# FIXME

Generate compiler warnings with FIXMEs. There are two ways to use FIXME, 

* as an expression within the body of a class/object/method
* as an annotation

```scala
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

# Using with SBT

In your SBT build settings add,

```scala
resolvers += "tysonjh releases" at "http://tysonjh.github.io/releases/"

libraryDependencies += "com.tysonjh" %% "fixme" % "0.2" // Scala 2.10.x (or)

libraryDependencies += "com.tysonjh" %% "fixme" % "1.1" // Scala 2.11.x
```
