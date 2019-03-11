package db

import com.datastax.driver.core.SocketOptions
import com.outworkers.phantom.dsl._
import config.CassandraSettings

object LogConnector extends CassandraSettings{
    val connector: CassandraConnection = ContactPoint.local
      .withClusterBuilder(_.withSocketOptions(
        new SocketOptions()
          .setConnectTimeoutMillis(connectionTimeout)
          .setReadTimeoutMillis(readTimeout)
      )
      ).noHeartbeat().keySpace(
      KeySpace(keyspace).ifNotExists()
        .`with`(replication eqs SimpleStrategy.replication_factor(replicationFactor))
        .and(durable_writes eqs true)
    )
}

object LogEventDB extends LogsDatabase(LogConnector.connector)

trait LogEventDBProvider extends LogsDBProvider {
  override def database: LogsDatabase = LogEventDB
}