object Test extends App {
  println("""
    | Usage: 
    | - FIXME("2014/04/10: force fixes by a certain date with compiler errors") \n
    | - FIXME("constant nagging with compiler warnings")\n
    | - @FIXME annotations work the same way!\n
    | - Like TODOs more? TODO works the same as FIXME!\n
  """.stripMargin)
}