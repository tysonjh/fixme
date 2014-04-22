import java.text.SimpleDateFormat
import java.util.Date
import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

object FIXME {
  def apply(format: String): Any = macro fixmeMacro.impl
}

class FIXME(format: String) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro fixmeMacro.implAnnot
}

object TODO {
  def apply(format: String): Any = macro fixmeMacro.impl
}

class TODO(format: String) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro fixmeMacro.implAnnot
}

object fixmeMacro  {
  private[this] val dateFormat = new SimpleDateFormat("yyyy/MM/dd")
  private[this] val FixmeFormat = """(\d{4}/\d{2}/\d{2})[\s]*:[\s]*([^\s].+)""".r
  private[this] val currentDate = new Date()

  def impl(c: Context)(format: c.Expr[String]): c.Expr[Any] = {
    import c.universe._
    val q"${ s_format: String }" = format.tree
    evaluate(c, s_format)
    c.Expr[Any](q"()")
  }

  def implAnnot(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val q"new $fixme_class(${ s_format: String })" = c.prefix.tree
    evaluate(c, s_format)
    c.Expr[Any](Block(annottees.map(_.tree).toList, Literal(Constant(()))))
  }

  private[this] def evaluate(c: Context, format: String) {
    try {
      format match {
        case FixmeFormat(date, message) =>
          if(currentDate.after(dateFormat.parse(date))) {
            c.abort(c.enclosingPosition, s"FIXME DATE PASSED ($date): $message")
          }
        case message => c.warning(c.enclosingPosition, s"FIXME: $message")
      }
    } catch {
      case any: Exception => c.abort(c.enclosingPosition, "Expected format: 'yyyy/MM/dd: message'")
    } 
  }
}


