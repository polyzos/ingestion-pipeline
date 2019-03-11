package utils

import java.util.Collections
import java.util.concurrent.LinkedBlockingQueue

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.consumer.KafkaConsumer
import thrift.logschema.LogEvent
import utils.KafkaProps.KafkaConsumerProps

import scala.util.{Failure, Success, Try}

object EventLogConsumer extends KafkaConsumerProps with LazyLogging {
  private val queue = new LinkedBlockingQueue[LogEvent]

  private lazy val kafkaConsumer = Try(new KafkaConsumer[String, LogEvent](consumerProps)) match {
    case Success(consumer) =>
      logger.info("Creating kafka consumer ..")
      Some(consumer)
    case Failure(exception)  =>
      logger.warn(s"Failed to instantiate kafka consumer: $exception")
      None
  }

  def readFromKafka(): LogEvent = {
    import scala.collection.JavaConverters._

    kafkaConsumer.foreach { consumer =>
      consumer.subscribe(Collections.singletonList(KafkaProps.topic))
      val results = consumer.poll(2000).asScala
      results.foreach { r =>  queue.add(r.value()) }
    }
    queue.poll()
  }
}
