package stock.app

import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import stock.{PropertiesFactory, TopologyBuilder}

object StreamsApp extends App {

  val props = PropertiesFactory.createDefault()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stock")

  val topology = TopologyBuilder.build()

  val streams: KafkaStreams = new KafkaStreams(topology, props)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(java.time.Duration.ofSeconds(10))
  }
}
