package faust

import scala.meta._
import scala.meta.contrib._

class WriteBackNFA extends Feature {
  around (q"val features = List(new AckIdle)") insert (q"val features = List(new AckRead, new DirtyAccounting)") in (q"trait HasWriteNFA") register
}
