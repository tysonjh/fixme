@FIXME("2013/04/10: This annotation will abort compilation if not fixed by 2013/04/10") 
object Test extends App {

  @FIXME("This annotation always generates a warning")
  def hi() {
    FIXME("2013/04/10: This expression will abort compilation if not fixed by 2013/04/10")
    println("hi")
  }
	
  FIXME("This expression always generates a warning")  
  hi()
}