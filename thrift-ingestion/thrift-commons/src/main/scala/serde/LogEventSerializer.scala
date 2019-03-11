package serde

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serializer
import thrift.logschema.LogEvent

import scala.util.{Failure, Success, Try}

/**
  * Serializer for the LogEvent Object
  * */
class LogEventSerializer extends Serializer[LogEvent] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def serialize(topic: String, data: LogEvent): Array[Byte] = {
    val objectMapper = new ObjectMapper()
    val retVal = Try(objectMapper.writeValueAsString(data).getBytes()) match {
      case Success(value) => value
      case Failure(exception) =>
        println(s"Failed to serialize log event $data. - $exception")
        Array.empty[Byte]
    }
    retVal
  }

  override def close(): Unit = {}
}
