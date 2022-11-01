package faust

import scala.meta._
import scala.meta.contrib._

class ICacheBufferBookeeping extends Feature {
  extendWithInit (init"InstructionCache(usedParams, nastiParams, coreParams.xlen)") insert (init"HasBufferBookeeping") in (q"class Tile") register
}
