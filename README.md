# FIXME

Generate compiler warnings with FIXMEs.

```scala
FIXME "2014-04-10: we need more awesome here"
```

Will generate a compiler warning

```scala
[warn] /Users/tysonjh/Development/fixme/core/src/main/scala/Test.scala:3: FIXME DATE PASSED (2014/04/10): we need more awesome here
[warn]     FIXME("2014-04-10: we need more awesome here")
[warn]          ^
[warn] one warning found
```