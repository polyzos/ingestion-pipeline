package db

import com.outworkers.phantom.builder.query.CreateQuery.Default
import com.outworkers.phantom.dsl._
import thrift.logschema.LogEvent

class LogsDatabase(override val connector: CassandraConnection)
  extends Database[LogsDatabase](connector) {
  object logs extends LogEventTable with Connector {
    override def autocreate(keySpace: KeySpace): Default[LogEventTable, LogEvent] = {
      create.ifNotExists()(keySpace)
        .`with`(
          compaction eqs LeveledCompactionStrategy
            .sstable_size_in_mb(50)
        )
    }
  }
}
