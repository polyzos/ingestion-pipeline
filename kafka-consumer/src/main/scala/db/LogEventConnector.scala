package db

import com.datastax.driver.core.SocketOptions
import com.outworkers.phantom.ResultSet
import com.outworkers.phantom.dsl._
import config.Settings
import thrift.logschema.LogEvent

import scala.concurrent.Future

object LogEventConnector extends Settings{
    val connector: CassandraConnection = ContactPoint.local
      .withClusterBuilder(_.withSocketOptions(
        new SocketOptions()
          .setConnectTimeoutMillis(connectionTimeout)
          .setReadTimeoutMillis(readTimeout)
      ))
      .noHeartbeat()
      .keySpace(
        KeySpace(keyspace)
          .ifNotExists()
          .`with`(
            replication eqs SimpleStrategy.replication_factor(replicationFactor))
          .and(durable_writes eqs true)
    )
}

object LogEventDB extends LogsDatabase(LogEventConnector.connector)

trait LogEventDBProvider extends DatabaseProvider[LogsDatabase]

trait LogEventServiceProvider extends LogEventDBProvider {
  override def database: LogsDatabase = LogEventDB

  def findByPollster(pollster: String): Future[Option[LogEvent]] =
    db.logs.findByPollster(pollster)

  def storeLog(log: LogEvent): Future[ResultSet] = {
    db.logs.store(log)
  }
}