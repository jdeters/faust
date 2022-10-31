package faust

import scala.meta._
import scala.meta.contrib._

class ExtendInitwithInit(oldCode: Init, newCode: Init = const.NullInit, context: Defn.Class = const.NullClass)(implicit feature: Feature)
  extends Advice(newCode, context) {

  def in(newContext: Defn.Class): Advice = {
    new ExtendInitwithInit(oldCode, newCode, newContext)
  }

  def insert(newNewCode: Tree): Advice = {
    new ExtendInitwithInit(oldCode, newNewCode.asInstanceOf[Init], context)
  }

  def advise = new Transformer {
    override def apply(tree: Tree): Tree = {
      tree match {
      case q"..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) $template"
        //if we've found the context OR we don't care about context, apply the code
        if (tname.value == context.name.value || context.name.value == const.NullClass.name.value) => {
          println("match found")
          val newTemplate: Template = applyCode(template).asInstanceOf[Template]
          q"..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) ${newTemplate}"
        }
      case _ => super.apply(tree)
     }
    }
  }

  def applyCode = new Transformer {
    override def apply(tree: Tree): Tree = {
      tree match {
        case q"new { ..$stat } with ..$inits { $self => ..$stats }" => {
          val contains = inits.foldLeft(false)((isInside, currentInnit) => currentInnit.isEqual(oldCode) || isInside)
          if(contains) q"new { ..$stat } with ..${inits ++ List(newCode)} { $self => ..$stats }"
          else q"new { ..$stat } with ..$inits { $self => ..$stats }"
        }
        case q"new $init" if(init.isEqual(oldCode))=> {
          q"new $init with $newCode"
        }
        case _ => super.apply(tree)
      }
    }
  }
}
