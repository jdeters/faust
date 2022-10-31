package faust

import scala.meta._
import scala.meta.contrib._

class SimpleConfig extends Feature {
  after (q"val core = Module(new Core(coreParams))") insert (q"val simpleConfig = CacheConfig(nWays = cacheParams.nWays, nSets = 1, blockBytes = cacheParams.blockBytes)") register
}
