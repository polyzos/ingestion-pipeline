package utils

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import thrift.logschema.LogEvent
import utils.KafkaProps.KafkaProducerProps

import scala.util.{Failure, Success, Try}

/**
  * Creates a kafka producer and handles the event ingestion
  * */
object EventLogProducer extends KafkaProducerProps with LazyLogging{

  private lazy val kafkaProducer = Try(new KafkaProducer[String, LogEvent](props)) match {
    case Success(producer)  => Some(producer)
    case Failure(exception) =>
      logger.warn(s"Failed to instantiate kafka producer: $exception")
      None
  }

  def writeToKafka(topic: String, value: LogEvent): Unit = {
    kafkaProducer.map { producer =>
      val producerRecord = new ProducerRecord(topic, value.pollster, value)
      producer.send(producerRecord)
    }
  }
}

