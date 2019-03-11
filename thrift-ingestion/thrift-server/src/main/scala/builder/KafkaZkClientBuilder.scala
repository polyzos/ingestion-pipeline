package builder

import kafka.zk.KafkaZkClient
import org.apache.kafka.common.utils.Time

import scala.util.{Failure, Success, Try}

/**
  * Creates a zk client
  * */
object KafkaZkClientBuilder {
  def apply(connectString: String,
            isSecure: Boolean,
            sessionTimeoutMs: Int,
            connectionTimeoutMs: Int,
            maxInFlightRequests: Int,
            time: Time = Time.SYSTEM) = {

    Try(KafkaZkClient(connectString, isSecure, sessionTimeoutMs, connectionTimeoutMs, maxInFlightRequests, Time.SYSTEM)) match {
      case Success(kzc) =>  Some(kzc)
      case Failure(e) =>    None
    }
  }
}
