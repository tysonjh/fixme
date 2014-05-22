package fixme

import java.text.SimpleDateFormat
import java.util.Date
import scala.language.experimental.macros
import scala.reflect.macros.Context
import scala.annotation.StaticAnnotation

object FIXME {

  /**
   * Create a FIXME. The format parameter affects behaviour.
   * FIXME("2014/04/10: this should be fixed") - generates a compiler error after the date has passed
   * FIXME("this should be fixed") - generates a compiler warning every compilation
   * @param format date:message where date is optional. e.g. "2014/04/10: fix me"
   */
  def apply(format: String): Any = macro fixmeMacro.delegateFixme
}

/**
 * Create a FIXME annotation. The format parameter affects behaviour.
 * - {@literal @}FIXME("2014/04/10: this should be fixed") - generates a compiler error after the date has passed
 * - {@literal @}FIXME("this should be fixed") - generates a compiler warning every compilation
 * @param format date:message where date is optional. e.g. "2014/04/10: fix me"
 */
class FIXME(format: String) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro fixmeMacro.delegateFixmeAnnot
}

object TODO {
  /**
   * A synonym for FIXME - see FIXME
   */
  def apply(format: String): Any = macro fixmeMacro.delegateTodo
}

/**
 * A synonym for FIXME - see FIXME annotation
 */
class TODO(format: String) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro fixmeMacro.delegateTodoAnnot
}

object fixmeMacro {
  private[this] val fixme = "FIXME"
  private[this] val todo = "TODO"
  private[this] val dateFormat = new SimpleDateFormat("yyyy/MM/dd")
  private[this] val FixmeFormat = """(\d{4}/\d{2}/\d{2})[\s]*:[\s]*([^\s].+)""".r
  private[this] val currentDate = new Date()

  def delegateTodoAnnot(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = implAnnot(c)(todo, annottees)

  def delegateTodo(c: Context)(format: c.Expr[String]): c.Expr[Any] = impl(c)(todo, format)

  def delegateFixmeAnnot(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = implAnnot(c)(fixme, annottees)

  def delegateFixme(c: Context)(format: c.Expr[String]): c.Expr[Any] = impl(c)(fixme, format)

  def impl(c: Context)(kind: String, format: c.Expr[String]): c.Expr[Any] = {
    import c.universe._
    val q"${ s_format: String }" = format.tree
    evaluate(c, kind, s_format)
    c.Expr[Any](q"()")
  }

  def implAnnot(c: Context)(kind: String, annottees: Seq[c.Expr[Any]]): c.Expr[Any] = {
    import c.universe._
    val q"new $fixme_class(${ s_format: String })" = c.prefix.tree
    evaluate(c, kind, s_format)
    c.Expr[Any](Block(annottees.map(_.tree).toList, Literal(Constant(()))))
  }

  private[this] def evaluate(c: Context, kind: String, format: String) {
    try {
      format match {
        case FixmeFormat(date, message) ⇒
          if (currentDate.after(dateFormat.parse(date))) {
            c.abort(c.enclosingPosition, s"$kind DATE PASSED ($date): $message")
          }
        case message ⇒ c.warning(c.enclosingPosition, s"$kind: $message")
      }
    } catch {
      case any: Exception ⇒ c.abort(c.enclosingPosition, "Expected format: 'yyyy/MM/dd: message'")
    }
  }
}

