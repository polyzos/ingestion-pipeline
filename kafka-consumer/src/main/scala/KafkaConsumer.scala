
import actors.CassandraWorkerActor
import actors.CassandraWorkerActor.StoreRequest
import akka.actor.ActorSystem
import akka.routing.RoundRobinPool
import com.typesafe.scalalogging.LazyLogging
import org.apache.thrift.TException
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.{TFramedTransport, TSocket}
import thrift.logschema.LogEventService
import com.outworkers.phantom.dsl._
import config.Settings
import db.{LogEventServiceProvider, LogEventDBProvider}

import scala.util.{Failure, Success, Try}

/**
  * KafkaConsumer is the entry point
  *    reads data from Kafka and stores them into Cassandra
  *    using actor workers
  * */
object KafkaConsumer extends LazyLogging with Settings{

  private[this] lazy val cassandraService = new LogEventServiceProvider with LogEventDBProvider

  private[this] implicit val system: ActorSystem = ActorSystem("kafka-consumer")

  /**
    * Creates a client to connect to the thrift server
    * consumes messages and sends them to actor workers
    * */
  private def invoke(): Unit = {
    logger.info("Starting Kafka Consumer ..")
    val cassandraPoolMaster = system.actorOf(
      RoundRobinPool(5)
        .props(CassandraWorkerActor.props(cassandraService)
        ), "cassandraPoolMaster")

    Try(new TFramedTransport(new TSocket(host, port))) match {
      case Success(transport) =>
        val protocol = new TBinaryProtocol(transport)
        val client = new LogEventService.Client(protocol)
        transport.open()

        while(true) {
          try {
            val logEvent = client.pullLogEvent()
            logger.info(s"[Client] received: $logEvent")
            cassandraPoolMaster ! StoreRequest(logEvent)
          } catch {
            case _: TException =>
          }
        }
      case Failure(exception) =>
        logger.warn(s"Failed to create connection: $exception")
    }
  }

  def main(args: Array[String]): Unit = {
    cassandraService.database.create()
    invoke()
  }
}
