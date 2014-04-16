import java.text.SimpleDateFormat
import java.util.Date
import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object FIXME {
  private[this] val date_format = new SimpleDateFormat("yyyy/MM/dd")
  private[this] val FixmeFormat = """(\d{4}/\d{2}/\d{2})[\s]*:[\s]*([^\s].+)""".r

  def apply(format: String) = macro impl

  def impl(c: Context)(format: c.Expr[String]): c.Expr[Any] = {
    import c.universe._
    val Literal(Constant(s_format: String)) = format.tree
    try {
      val curDate = new Date()
      s_format match {
        case FixmeFormat(date, message) =>
          if(curDate.after(date_format.parse(date))) {
            c.warning(c.enclosingPosition, s"FIXME DATE PASSED ($date): $message")
          }
      }
    } catch {
      case any: Exception => c.abort(c.enclosingPosition, "Expected format: 'yyyy/MM/dd: message'")
    }
    reify()
  }
}

