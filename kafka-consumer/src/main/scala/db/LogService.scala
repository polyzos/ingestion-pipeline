package db

import com.outworkers.phantom.ResultSet
import com.outworkers.phantom.dsl.{DatabaseProvider, KeySpace, Session}
import thrift.logschema.LogEvent

import scala.concurrent.Future

trait RootConnector {

  implicit def space: KeySpace

  implicit def session: Session
}


trait LogsDBProvider extends DatabaseProvider[LogsDatabase]


trait LogService extends LogsDBProvider {
  override def database: LogsDatabase = LogEventDB

  def findByPollster(pollster: String): Future[Option[LogEvent]] =
    db.logs.findByPollster(pollster)

  def storeLog(log: LogEvent): Future[ResultSet] = {
    db.logs.store(log)
  }

}
