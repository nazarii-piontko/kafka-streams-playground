package wordCount

import java.time.Duration
import java.util.Properties

import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object StreamsApp extends App {

  private def getKafkaProps = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props
  }

  val topology = TopologyBuilder.build

  val streams: KafkaStreams = new KafkaStreams(topology, getKafkaProps)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }
}
