import com.typesafe.scalalogging.LazyLogging
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.{TFramedTransport, TSocket}
import thrift.logschema.LogEventService

import scala.util.{Failure, Success, Try}

object ThriftClient extends LazyLogging {

  private def invoke(): Unit = {
    logger.info("Starting Thrift Client ...")
    Try(new TFramedTransport(new TSocket("thrift-server", 9090))) match {
      case Success(transport) =>
        val protocol = new TBinaryProtocol(transport)

        val client = new LogEventService.Client(protocol)
        transport.open()
        while(true) {
          val logEvent = LogEventGenerator.generateLogEvent()
          Try(client.pushLogEvent(logEvent)) match {
            case Success(_) =>
              logger.info("[Client] Sent to server log event: " + logEvent)
            case Failure(e) =>
              logger.info(s"Failed to push log event '$logEvent to the server. $e")
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
