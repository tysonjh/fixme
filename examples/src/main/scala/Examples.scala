import fixme._

@TODO("2013/04/10: This annotation will abort compilation if not done by 2013/04/10") 
object TodoExample {
  
  @TODO("This annotation always generates a warning")
  def apply() {
    TODO("2013/04/10: This expression will abort compilation if not done by 2013/04/10")
    println("todo")
  }
}



@FIXME("2013/04/10: This annotation will abort compilation if not fixed by 2013/04/10") 
object FixmeExample {

  @FIXME("This annotation always generates a warning")
  def apply() {
    FIXME("2013/04/10: This expression will abort compilation if not fixed by 2013/04/10")
    println("fixme")
  }
}