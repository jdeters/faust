package faust

import scala.meta._
import scala.meta.contrib._

class SimpleConfig extends Feature {
  around (q"val usedParams = cacheParams") insert (q"val usedParams = CacheConfig(nWays = cacheParams.nWays, nSets = 1, blockBytes = cacheParams.blockBytes)") register
}
