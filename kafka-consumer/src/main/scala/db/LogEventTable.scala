package db

import com.outworkers.phantom.dsl._
import org.joda.time.format.DateTimeFormat
import thrift.logschema.LogEvent

import scala.concurrent.Future

abstract class LogEventTable extends Table[LogEventTable, LogEvent] {
  object version extends IntColumn
  object currentDate extends DateTimeColumn with ClusteringOrder with Ascending
  object startDate extends DateTimeColumn
  object endDate extends DateTimeColumn
  object pollster extends StringColumn with PartitionKey
  object favor extends IntColumn
  object oppose extends IntColumn
  object total extends IntColumn
  object url extends StringColumn
  object device extends StringColumn
  object message extends StringColumn

  def findByPollster(pollster: String): Future[Option[LogEvent]] = {
    select.where(_.pollster eqs pollster).one()
  }

  def store(record: LogEvent): Future[ResultSet] = {
    insert.value(_.version, record.version.asInstanceOf[Int])
      .value(_.currentDate, parseDate(record.currentDate, "EEE MMM dd HH:mm:ss ZZZ yyyy"))
      .value(_.startDate, parseDate(record.startDate, "dd/MM/YYYY"))
      .value(_.endDate,  parseDate(record.startDate, "dd/MM/YYYY"))
      .value(_.pollster, record.pollster)
      .value(_.favor, record.favor)
      .value(_.oppose, record.oppose)
      .value(_.total, record.oppose)
      .value(_.url, record.url)
      .value(_.device, record.device.toString)
      .value(_.message, record.message.toString)
      .future()
  }

  private def parseDate(date: String, pattern: String) = {
    DateTimeFormat
      .forPattern(pattern)
      .parseDateTime(date)
  }
}