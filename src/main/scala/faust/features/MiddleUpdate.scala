package faust

import scala.meta._
import scala.meta.contrib._

class MiddleUpdate extends Feature {
  extendWithInit (init"DataCache(dCacheConfig, nastiParams, coreParams.xlen)") insert (init"HasMiddleUpdate") in (q"class Tile") register
}
