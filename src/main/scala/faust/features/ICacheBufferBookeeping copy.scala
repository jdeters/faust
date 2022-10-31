package faust

import scala.meta._
import scala.meta.contrib._

class ICacheBufferBookeeping extends Feature {
  around (q"val icache = Module(new InstructionCache(cacheParams, nastiParams, coreParams.xlen))") insert (q"val icache = Module(new InstructionCache(simpleConfig, nastiParams, coreParams.xlen))") register

  extendWithInit (init"InstructionCache(simpleConfig, nastiParams, coreParams.xlen)") insert (init"HasBufferBookeeping") in (q"class Tile") register
}
