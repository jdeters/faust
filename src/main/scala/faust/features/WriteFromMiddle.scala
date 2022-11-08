package faust

import scala.meta._
import scala.meta.contrib._

class WriteFromMiddle extends Feature {

  around (q"val writeAddress = (address(p.xlen - 1, p.offsetLen) << p.offsetLen.U).asUInt") insert (q"val writeAddress = (Cat(oldTag, index) << p.offsetLen.U).asUInt") in (q"trait HasSimpleWrite") register

  around (q"val writeData = data") insert (q"val writeData = readData") in (q"trait HasSimpleWrite") register

  around (q"val writeMask = Some(mask)") insert (q"val writeMask = None") in (q"trait HasSimpleWrite") register
}
