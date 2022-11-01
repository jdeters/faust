package faust

import scala.meta._
import scala.meta.contrib._

class ExtendClass(oldCode: Defn.Class, newCode: Init = const.NullInit, context: Defn.Class = const.NullClass)
  (implicit feature: Feature) extends Advice(newCode, context) {

  def in(newContext: Defn): Advice = {
    new ExtendClass(oldCode, newCode, newContext.asInstanceOf[Defn.Class])
  }

  def insert(newNewCode: Tree): Advice = {
    new ExtendClass(oldCode, newNewCode.asInstanceOf[Init], context)
  }

  def advise = new Transformer {
    override def apply(tree: Tree): Tree = {
      tree match {
      //if there's no context to identify a subclass to modify, direcly modify class
      case q"..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) extends { ..$stats } with ..$inits { $self => ..$bodyStats }"
        if (tname.value == oldCode.name.value && context.name.value == const.NullClass.name.value) => applyCode(tree)
      //if we've found the context class, pass the rest of the tree for modification
      case q"..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) extends { ..$stats } with ..$inits { $self => ..$bodyStats }"
        if (tname.value == context.name.value || context.name.value == const.NullClass.name.value) => {
          val newStats: List[Stat] = applyCode(bodyStats).asInstanceOf[List[Stat]]
          q"..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) extends { ..$stats } with ..$inits { $self => ..$newStats }"
        }
      case _ => super.apply(tree)
     }
    }
  }

  def applyCode = new Transformer {
    override def apply(tree: Tree): Tree = {
      tree match {
        case q"..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) extends { ..$stats } with ..$inits { $self => ..$bodyStats }"
          if (tname.value == oldCode.name.value) => {
            //build a new template with the new init that we want
            q"..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) extends { ..$stats } with ..${inits ++ List(newCode)} { $self => ..$bodyStats }"
          }
        case _ => super.apply(tree)
      }
    }
  }
}
