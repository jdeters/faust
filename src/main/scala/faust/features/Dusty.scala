package faust

import scala.meta._
import scala.meta.contrib._

class Dusty extends Feature {
  before (q"val isDirty = dirty(index)") insert (q"""
    val dusty = new Middleend(fsmHandle, p, address, tag, index, valids)
    val (_, dustyData) = dusty.read(buffer, nextAddress, offset)
    dusty.allocate(Cat(mainMem.r.bits.data, Cat(buffer.init.reverse)), readDone)
  """) in (q"trait HasMiddleUpdate") register

  around (q"val isDirty = dirty(index)") insert (q"val isDirty = dirty(index) && (dustyData =/= readData)") in (q"trait HasMiddleUpdate") register
}
