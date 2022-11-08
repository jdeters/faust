package faust

import scala.meta._
import scala.meta.contrib._

class WriteBackNFA extends Feature {
  around (q"override lazy val cacheNFA = Weaver[NFA](List(new AckIdle), ReadFSM() + WriteFSM(), (before: NFA, after: NFA) => before.isEqual(after))") insert (q"override lazy val cacheNFA = Weaver[NFA](List(new AckRead, new DirtyAccounting), ReadFSM() + WriteFSM(), (before: NFA, after: NFA) => before.isEqual(after))") in (q"trait HasWriteNFA") register
}
