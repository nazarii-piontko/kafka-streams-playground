package stock.app

import java.time.Duration

import io.razem.influxdbclient.Parameter.Precision
import io.razem.influxdbclient.{InfluxDB, Point}
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import stock.json.JsonDeserializer
import stock.{Bar, PropertiesFactory, Topics}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.jdk.CollectionConverters._

class BarDeserializer extends JsonDeserializer[Bar]

object InfluxDbWriterApp extends App {
  @volatile
  var stop = false

  sys.addShutdownHook {
    stop = true
  }

  val dbConnection = InfluxDB.connect("localhost", 8086)
  val db = dbConnection.selectDatabase("bars")

  val props = PropertiesFactory.createDefault()
  props.put("key.deserializer", classOf[StringDeserializer])
  props.put("value.deserializer", classOf[BarDeserializer])
  props.put("enable.auto.commit", "false")
  props.put("group.id", "stock-bars-influxdb-writer")

  val consumer = new KafkaConsumer[String, Bar](props)
  try {
    consumer.subscribe(List(Topics.barsMin1).asJava)

    while (!stop) {
      val records = consumer.poll(Duration.ofSeconds(1))

      if (!records.isEmpty) {
        val points = records.asScala.map(r => {
          val bar = r.value()
          Point("min1", timestamp = r.timestamp())
            .addTag("symbol", r.key())
            .addField("open", bar.open)
            .addField("high", bar.high)
            .addField("low", bar.low)
            .addField("close", bar.close)
            .addField("volume", bar.volume)
        }).toList

        db.bulkWrite(points, precision = Precision.MILLISECONDS)

        consumer.commitSync()
      }
    }
  } finally {
    consumer.close()
  }
}
