# FIXME

Generate compiler warnings with FIXMEs.

```scala
FIXME("2014/04/10: This will generate a warning if not fixed by 2014/04/10")
```

Will generate a compiler warning

```scala
[warn] /Users/tysonjh/Development/fixme/core/src/main/scala/Test.scala:7: FIXME DATE PASSED (2014/04/10): This will generate a warning if not fixed by 2014/04/10
[warn]   FIXME("2014/04/10: This will generate a warning if not fixed by 2014/04/10")
[warn]        ^
[warn] one warning found
```

Generate compiler errors with FIXME.orDie.

```scala
FIXME.orDie("2014/04/10: This will abort compilation if not fixed by 2014/04/10")
```

Will generate a compiler error

```scala
[error] /Users/tysonjh/Development/fixme/core/src/main/scala/Test.scala:3: FIXME.orDie DATE PASSED (2014/04/10): This will abort compilation if not fixed by 2014/04/10
[error]     FIXME.orDie("2014/04/10: This will abort compilation if not fixed by 2014/04/10")
[error]                ^
[error] one error found
[error] (core/compile:compile) Compilation failed
```