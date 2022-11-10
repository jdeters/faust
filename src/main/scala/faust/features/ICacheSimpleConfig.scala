package faust

import scala.meta._
import scala.meta.contrib._

class ICacheSimpleConfig extends Feature {
  around (q"val iCacheConfig = cacheParams") insert (q"val iCacheConfig = CacheConfig(nWays = cacheParams.nWays, nSets = 1, blockBytes = cacheParams.blockBytes)") register
}
