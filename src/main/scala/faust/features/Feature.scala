package faust

import scala.meta._

abstract class Feature (){
  implicit val feature = this
  val adviceList = scala.collection.mutable.ListBuffer[Transformer]()

  protected def before(oldCode: Stat) = new Before(oldCode)
  protected def after(oldCode: Stat) = new After(oldCode)
  protected def around(oldCode: Stat) = new Around(oldCode)
  protected def extendWithStat(oldCode: Init) = new ExtendInitwithStat(oldCode)
  protected def extendWithInit(oldCode: Init) = new ExtendInitwithInit(oldCode)
  protected def extend(oldCode: Defn.Class) = new ExtendClass(oldCode)
  protected def extend(oldCode: Defn.Trait) = new ExtendTrait(oldCode)

  def apply(tree: Tree): Tree = {
    adviceList.foldLeft(tree){ (newTree, transform) => { transform(newTree) } }
  }

}
