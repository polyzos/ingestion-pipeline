import builder.KafkaZkClientBuilder
import com.typesafe.scalalogging.LazyLogging
import config.ServerSettings
import kafka.zk.AdminZkClient
import org.apache.thrift.server.TNonblockingServer
import org.apache.thrift.transport.TNonblockingServerSocket
import services.LogEventServiceImpl
import thrift.logschema.LogEventService

import scala.util.{Failure, Success, Try}

/**
  * Thrift Server is the entry point of the server module
  *   1. Connect to kafka
  *   2. Creates the necessary topic
  *   3. Launches the server and waits for incoming requests
  * */
object ThriftServer extends ServerSettings with LazyLogging {

  private def start(): Unit = {
    logger.info("Starting Thrift Server..")
    val kafkaZkClient = KafkaZkClientBuilder(
      connectString,
      isSecure,
      sessionTimeoutMs,
      connectionTimeoutMS,
      maxInFlightRequests)

    // Creates a topic on kafka
    Try(new AdminZkClient(kafkaZkClient.get)) match {
      case Success(adminZkClient) =>
        if (!kafkaZkClient.get.topicExists(topic)) {
          logger.info(s"[Server] Creating topic '$topic'.")
          adminZkClient.createTopic(topic, partitions, replicationFactor)
        } else {
          logger.info(s"[Server] Topic '$topic' already exists.")
        }
      case Failure(_) =>
        logger.error(
          s"""
             |
             |Failed to create AdminZKClient.
             |Please make sure your kafka cluster is Running.
          """.stripMargin)
        System.exit(0)
    }

    // Setup the server
    Try(new TNonblockingServerSocket(serverPort)) match {
      case Success(transport) =>
        val processor = new LogEventService.Processor(new LogEventServiceImpl())
        val server = new TNonblockingServer(new TNonblockingServer.Args(transport).processor(processor))
        logger.info("[Server] Running on port 9090:")
        logger.info("[Server] Waiting for incoming requests ...")
        server.serve()
      case Failure(exception) =>
        logger.error(s"Server Error: $exception")
    }
  }

  def main(args: Array[String]): Unit = {
    start()
  }
}
