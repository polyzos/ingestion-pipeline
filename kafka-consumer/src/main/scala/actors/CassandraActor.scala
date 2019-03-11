package actors

import actors.CassandraActor.StoreRequest
import akka.actor.{Actor, ActorLogging}
import db.{LogEventDBProvider, LogService}
import thrift.logschema.LogEvent
import com.outworkers.phantom.dsl._

object CassandraActor {
  case class StoreRequest(logEvent: LogEvent)
}

class CassandraActor extends Actor with ActorLogging{

  private val service = new LogService with LogEventDBProvider {}

  override def preStart(): Unit = {
    service.database.create()(context.dispatcher)
  }

  override def receive: Receive = {
    case StoreRequest(logEvent)=>
      service.storeLog(logEvent)
      println(s"[Cassandra Actor] Received $logEvent")
  }
}
