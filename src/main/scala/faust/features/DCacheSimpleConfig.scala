package faust

import scala.meta._
import scala.meta.contrib._

class DCacheSimpleConfig extends Feature {
  around (q"val dCacheConfig = cacheParams") insert (q"val dCacheConfig = CacheConfig(nWays = cacheParams.nWays, nSets = 1, blockBytes = cacheParams.blockBytes)") register
}
