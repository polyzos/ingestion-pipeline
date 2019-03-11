package config

import com.typesafe.config.ConfigFactory

trait Settings {

  private val config = ConfigFactory.load()

  private val cassandraConf = config.getConfig("cassandra")

  lazy val keyspace: String       = cassandraConf.getString("keyspace")

  lazy val connectionTimeout: Int = cassandraConf.getInt("connectionTimeout")
  lazy val readTimeout: Int       = cassandraConf.getInt("readTimeout")
  lazy val replicationFactor: Int = cassandraConf.getInt("replicationFactor")

  private val clientConfig = ConfigFactory.load().getConfig("client")

  lazy val host: String = clientConfig.getString("host")
  lazy val port: Int = clientConfig.getInt("port")
}
