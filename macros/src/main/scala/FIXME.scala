import java.text.SimpleDateFormat
import java.util.Date
import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

object FIXME {
  def apply(format: String): Any = macro fixmeMacro.impl
  def orDie(format: String): Any = macro fixmeMacro.implOrDie
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
  private[this] val date_format = new SimpleDateFormat("yyyy/MM/dd")
  private[this] val FixmeFormat = """(\d{4}/\d{2}/\d{2})[\s]*:[\s]*([^\s].+)""".r

  def impl(c: Context)(format: c.Expr[String]): c.Expr[Any] = {
    import c.universe._
    val q"${ s_format: String }" = format.tree
    maybeWarn(c, s_format)
    c.Expr[Any](q"()")
  }

  def implOrDie(c: Context)(format: c.Expr[String]): c.Expr[Any] = {
    import c.universe._
    val q"${ s_format: String }" = format.tree
    maybeAbort(c, s_format)
    c.Expr[Any](q"()")
  }

  def implAnnot(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val q"new $fixme_class(${ s_format: String })" = c.prefix.tree
    maybeWarn(c, s_format)
    c.Expr[Any](Block(annottees.map(_.tree).toList, Literal(Constant(()))))
  }

  private[this] def maybeWarn(c: Context, format: String) {
    try {
      val curDate = new Date()
      format match {
        case FixmeFormat(date, message) =>
          if(curDate.after(date_format.parse(date))) {
            c.warning(c.enclosingPosition, s"FIXME DATE PASSED ($date): $message")
          }
      }
    } catch {
      case any: Exception => c.abort(c.enclosingPosition, "Expected format: 'yyyy/MM/dd: message'")
    }
  }

  private[this] def maybeAbort(c: Context, format: String) {
    try {
      val curDate = new Date()
      format match {
        case FixmeFormat(date, message) =>
          if(curDate.after(date_format.parse(date))) {
            c.abort(c.enclosingPosition, s"FIXME.orDie DATE PASSED ($date): $message")
          }
      }
    } catch {
      case any: Exception => c.abort(c.enclosingPosition, "Expected format: 'yyyy/MM/dd: message'")
    }
  }
}


