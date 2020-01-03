package wordCount

import java.util.Properties

import org.apache.kafka.common.serialization.{LongDeserializer, StringDeserializer, StringSerializer}
import org.apache.kafka.streams.{KeyValue, StreamsConfig, TestInputTopic, TestOutputTopic, Topology, TopologyTestDriver}
import org.scalatest.{BeforeAndAfter, FunSuite}

class WordCountTestWithTopologyTestDriver extends FunSuite with BeforeAndAfter {

  var topology: Topology = _
  var driver: TopologyTestDriver = _
  var inputTopic: TestInputTopic[String, String] = _
  var outputTopic: TestOutputTopic[String, java.lang.Long] = _

  before {
    val props = new Properties
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234")

    topology = TopologyBuilder.build()
    driver = new TopologyTestDriver(topology, props)

    inputTopic = driver.createInputTopic(TopologyBuilder.inputStreamName,
      new StringSerializer,
      new StringSerializer)

    outputTopic = driver.createOutputTopic[String, java.lang.Long](TopologyBuilder.outputStreamName,
      new StringDeserializer,
      new LongDeserializer)
  }

  after {
    driver.close()
  }

  test("Test with unique words") {
    inputTopic.pipeInput(null, "Hello World")

    val kvList = outputTopic.readKeyValuesToList()

    kvList == List(
      KeyValue.pair("hello", 1),
      KeyValue.pair("world", 1)
    )
  }

  test("Test with duplicated words") {
    inputTopic.pipeInput(null, "Hello World")
    inputTopic.pipeInput(null, "Wonderful World")

    val kvList = outputTopic.readKeyValuesToList()

    kvList == List(
      KeyValue.pair("hello", 1),
      KeyValue.pair("world", 1),
      KeyValue.pair("wonderful", 1),
      KeyValue.pair("world", 2)
    )
  }
}
