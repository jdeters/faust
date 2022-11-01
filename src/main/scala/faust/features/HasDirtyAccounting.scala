package faust

import scala.meta._
import scala.meta.contrib._

class HasDirtyAccounting extends Feature {
  extend (q"class Cache") insert (init"HasDirtyAccounting") register
}
