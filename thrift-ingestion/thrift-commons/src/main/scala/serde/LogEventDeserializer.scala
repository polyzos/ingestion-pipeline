package serde

import java.util

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import org.apache.kafka.common.serialization.Deserializer
import thrift.logschema.LogEvent

import scala.util.{Failure, Success, Try}

class LogEventDeserializer extends Deserializer[LogEvent] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): LogEvent = {
    val mapper = new ObjectMapper()
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    val logEvent = Try(mapper.readValue(data, classOf[LogEvent])) match {
      case Success(value) => Some(value)
      case Failure(exception) =>
        println(s"Failed to deserialize log event $data. - $exception")
        None
    }
    logEvent.orNull
  }

  override def close(): Unit = {}
}
