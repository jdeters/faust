package faust

import scala.meta._
import scala.meta.contrib._

class DCacheMiddleAllocate extends Feature {
  extendWithInit (init"DataCache(usedParams, nastiParams, coreParams.xlen)") insert (init"HasMiddleAllocate") in (q"class Tile") register
}
