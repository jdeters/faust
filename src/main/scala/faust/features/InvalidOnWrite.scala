package faust

import scala.meta._
import scala.meta.contrib._

class InvalidOnWrite extends Feature {
  extendWithInit (init"DataCache(dCacheConfig, nastiParams, coreParams.xlen)") insert (init"HasInvalidOnWrite") in (q"class Tile") register
}
