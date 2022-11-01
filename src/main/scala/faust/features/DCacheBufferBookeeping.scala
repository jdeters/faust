package faust

import scala.meta._
import scala.meta.contrib._

class DCacheBufferBookeeping extends Feature {
  extendWithInit (init"DataCache(usedParams, nastiParams, coreParams.xlen)") insert (init"HasBufferBookeeping") in (q"class Tile") register
}
