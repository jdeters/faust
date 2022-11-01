package faust

import scala.meta._
import scala.meta.contrib._

class MiddleUpdate extends Feature {
  extendWithInit (init"DataCache(usedParams, nastiParams, coreParams.xlen)") insert (init"HasMiddleUpdate") in (q"class Tile") register
}
