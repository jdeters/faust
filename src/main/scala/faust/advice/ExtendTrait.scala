package faust

import scala.meta._
import scala.meta.contrib._

class ExtendTrait(oldCode: Defn.Trait, newCode: Init = const.NullInit, context: Defn = const.NullClass)
  (implicit feature: Feature) extends Advice(newCode, context) {

  def in(newContext: Defn): Advice = {
    new ExtendTrait(oldCode, newCode, newContext.asInstanceOf[Defn.Class])
  }

  def insert(newNewCode: Tree): Advice = {
    new ExtendTrait(oldCode, newNewCode.asInstanceOf[Init], context)
  }

  def advise = new Transformer {
    override def apply(tree: Tree): Tree = {
      tree match {
      //if there's no context to identify a subclass to modify, direcly modify class
      case q"..$mods trait $tname[..$tparams] extends { ..$stats } with ..$inits { $self => ..$bodyStats }"
        if (tname.value == oldCode.name.value) => applyCode(tree)
      //if we've found the context class, pass the rest of the tree for modification
      case _ => super.apply(tree)
     }
    }
  }

  def applyCode = new Transformer {
    override def apply(tree: Tree): Tree = {
      tree match {
        case q"..$mods trait $tname[..$tparams] extends { ..$stats } with ..$inits { $self => ..$bodyStats }"
          if (tname.value == oldCode.name.value) => {
            //build a new template with the new init that we want
            q"..$mods trait $tname[..$tparams] extends { ..$stats } with ..${inits ++ List(newCode)} { $self => ..$bodyStats }"
          }
        case _ => super.apply(tree)
      }
    }
  }
}
