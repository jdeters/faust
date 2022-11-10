package faust

import scala.meta._
import scala.meta.contrib._

class DCacheMiddleAllocate extends Feature {
  extendWithInit (init"DataCache(dCacheConfig, nastiParams, coreParams.xlen)") insert (init"HasMiddleAllocate") in (q"class Tile") register
}
