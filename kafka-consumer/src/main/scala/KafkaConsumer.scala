import actors.CassandraActor
import actors.CassandraActor.StoreRequest
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.apache.thrift.TException
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.{TFramedTransport, TSocket}
import thrift.logschema.LogEventService

import scala.util.{Failure, Success, Try}

object KafkaConsumer extends LazyLogging{

  private[this] implicit val system = ActorSystem("kafka-consumer")
  private[this] lazy val clientConfg = ConfigFactory.load().getConfig("client")

  private def invoke(): Unit = {
    logger.info("Starting Kafka Consumer ..")
    val cassandraActor = system.actorOf(Props[CassandraActor], "cassandra-actor")

    val host = clientConfg.getString("host")
    val port = clientConfg.getInt("port")

    Try(new TFramedTransport(new TSocket(host, port))) match {
      case Success(transport) =>
        val protocol = new TBinaryProtocol(transport)
        val client = new LogEventService.Client(protocol)
        transport.open()

        while(true) {
          try {
            val logEvent = client.pullLogEvent()
            logger.info(s"[Client] received: $logEvent")
            cassandraActor ! StoreRequest(logEvent)
          } catch {
            case _: TException =>
          }
        }
      case Failure(exception) =>
        logger.warn(s"Failed to create connection: $exception")
    }
  }

  def main(args: Array[String]): Unit = {
    invoke()
  }
}
