package stock

import java.util.Properties

import org.apache.kafka.streams.StreamsConfig

object PropertiesFactory {
  private val props: Properties = new Properties()

  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")

  def createDefault(): Properties = props.clone().asInstanceOf[Properties]
}
