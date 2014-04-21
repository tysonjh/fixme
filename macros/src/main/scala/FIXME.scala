import java.text.SimpleDateFormat
import java.util.Date
import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros

object FIXME {
  def apply(format: String): Any = macro fixmeMacro.impl
  def orDie(format: String): Any = macro fixmeMacro.implOrDie
}

object TODO {
  def apply(format: String): Any = macro fixmeMacro.impl
}

object fixmeMacro  {
  private[this] val date_format = new SimpleDateFormat("yyyy/MM/dd")
  private[this] val FixmeFormat = """(\d{4}/\d{2}/\d{2})[\s]*:[\s]*([^\s].+)""".r

  def impl(c: Context)(format: c.Expr[String]): c.Expr[Any] = {
    import c.universe._
    val q"${ s_format: String }" = format.tree
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
    c.Expr[Any](q"()")
  }

  def implOrDie(c: Context)(format: c.Expr[String]): c.Expr[Any] = {
    import c.universe._
    val q"${ s_format: String }" = format.tree
    try {
      val curDate = new Date()
      s_format match {
        case FixmeFormat(date, message) =>
          if(curDate.after(date_format.parse(date))) {
            c.abort(c.enclosingPosition, s"FIXME.orDie DATE PASSED ($date): $message")
          }
      }
    } catch {
      case any: Exception => c.abort(c.enclosingPosition, "Expected format: 'yyyy/MM/dd: message'")
    }
    c.Expr[Any](q"()")
  }
}


