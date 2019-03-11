package actors
import akka.actor.{Actor, ActorLogging, Props}
import db.{LogEventDBProvider, LogEventServiceProvider}
import thrift.logschema.LogEvent


/**
  * CassandraWorkerActor: Receives log event messages
  *   and stores them to cassandra
  * */
object CassandraWorkerActor {
  case class StoreRequest(logEvent: LogEvent)

  def props(cassandraService: LogEventServiceProvider with LogEventDBProvider) = Props(
    new CassandraWorkerActor(cassandraService)
  )
}

class CassandraWorkerActor(cassandraService: LogEventServiceProvider with LogEventDBProvider)
  extends Actor
    with ActorLogging {

  import CassandraWorkerActor._

  override def receive: Receive = {
    case StoreRequest(logEvent)=>
      cassandraService.storeLog(logEvent)
      log.info(s"[Cassandra Actor] Received $logEvent")
  }
}
