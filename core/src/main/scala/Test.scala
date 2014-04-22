@FIXME("2073/04/10: This will abort compilation if not fixed by 2073/04/10") 
object Test extends App {
  def hi() {
    FIXME.orDie("2073/04/10: This will abort compilation if not fixed by 2073/04/10")
    println("hi")
  }
	
  FIXME("2073/04/10: This will generate a warning if not fixed by 2073/04/10")  
  hi()
}