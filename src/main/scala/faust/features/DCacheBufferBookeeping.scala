package faust

import scala.meta._
import scala.meta.contrib._

class DCacheBufferBookeeping extends Feature {
  around (q"val dcache = Module(new DataCache(cacheParams, nastiParams, coreParams.xlen))") insert (q"val dcache = Module(new DataCache(simpleConfig, nastiParams, coreParams.xlen))") register

  extendWithInit (init"DataCache(simpleConfig, nastiParams, coreParams.xlen)") insert (init"HasBufferBookeeping") in (q"class Tile") register
}
