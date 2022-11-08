package faust

import scala.meta._
import scala.meta.contrib._

class HasDirtyAccounting extends Feature {
  extend (q"trait HasMiddleUpdate") insert (init"HasDirtyAccounting") register
  
  extend (q"trait HasSimpleWrite") insert (init"HasMiddleAllocate") register
}
