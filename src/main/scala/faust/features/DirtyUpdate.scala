package faust

import scala.meta._
import scala.meta.contrib._

class DirtyUpdate extends Feature {
  val handleSting = "sWriteCache"
  around (q"val updateCond = fsmHandle($handleSting) && hit && !cpu.abort") insert (q"val updateCond = fsmHandle($handleSting) && (hit || readJustDone) && !cpu.abort") in (q"trait HasMiddleUpdate") register

  after (q"middle.update(data, mask, updateCond)") insert (q"""
    val isDirty = dirty(index) 

    when(updateCond) {
      dirty := dirty.bitSet(index, true.B)
    }

    when(fsmHandle("sWriteWait")) {
      dirty := dirty.bitSet(index, false.B)
    }

    fsmHandle("doWrite") := mask.asUInt.orR && readDone
    fsmHandle("dirtyMiss") := !hit && isDirty
    fsmHandle("cleanMiss") := !localWrite && !isDirty

    dirtyRead := isDirty
  
    dirtyWrite := isDirty
    
    writeDone := hit || readJustDone

    localWrite := hit || readJustDone || cpu.abort

    isRead := !mask.asUInt.orR
  """) in (q"trait HasMiddleUpdate") register
}
