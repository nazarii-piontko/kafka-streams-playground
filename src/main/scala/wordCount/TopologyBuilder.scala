package wordCount

import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.Serdes._

object TopologyBuilder {
  val inputStreamName = "text_lines"
  val outputStreamName = "word_count_results"

  def build(): Topology = {
    val builder: StreamsBuilder = new StreamsBuilder

    builder.stream[String, String](inputStreamName)
      .flatMapValues(textLine => textLine.toLowerCase.split("\\W+"))
      .groupBy((_, word) => word)
      .count()
      .toStream
      .to(outputStreamName)

    builder.build()
  }
}
