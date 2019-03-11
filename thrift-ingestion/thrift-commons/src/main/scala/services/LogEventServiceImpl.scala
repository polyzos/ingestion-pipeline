package services

import com.typesafe.scalalogging.LazyLogging
import thrift.logschema.{LogEvent, LogEventService}
import utils.{EventLogConsumer, EventLogProducer, KafkaProps}

/**
  * Implementation of the services from thrift definition
  * */
class LogEventServiceImpl extends LogEventService.Iface with LazyLogging{

  override def pushLogEvent(logEvent: LogEvent): Unit = {
    logger.info(s"[Server] Received: $logEvent")
    EventLogProducer.writeToKafka(KafkaProps.topic, logEvent)
  }

  override def pullLogEvent(): LogEvent = {
    val record = EventLogConsumer.readFromKafka()
    logger.info(s"[Server] consumed record: $record")
    record
  }
}
