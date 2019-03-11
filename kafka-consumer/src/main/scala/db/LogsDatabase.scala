package db

import com.outworkers.phantom.dsl._

class LogsDatabase(override val connector: CassandraConnection)
  extends Database[LogsDatabase](connector) {
  object logs extends LogEventTable with Connector
}
