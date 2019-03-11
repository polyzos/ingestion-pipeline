package config

import com.typesafe.config.ConfigFactory

trait ServerSettings {

  private val config = ConfigFactory.load()

  private val zookeeperConf           = config.getConfig("zookeeper")

  lazy val connectString: String      = zookeeperConf.getString("connectString")
  lazy val isSecure: Boolean          = zookeeperConf.getBoolean("isSecure")
  lazy val sessionTimeoutMs: Int      = zookeeperConf.getInt("sessionTimeoutMs")
  lazy val connectionTimeoutMS: Int   = zookeeperConf.getInt("connectionTimeoutMs")
  lazy val maxInFlightRequests: Int   = zookeeperConf.getInt("maxInFlightRequests")

  private val kafkaConf               = config.getConfig("kafka")

  lazy val topic: String              = kafkaConf.getString("topic")
  lazy val partitions: Int            = kafkaConf.getInt("partitions")
  lazy val replicationFactor: Int     = kafkaConf.getInt("replicationFactor")

  lazy val serverPort: Int            = config.getConfig("server").getInt("port")
}
