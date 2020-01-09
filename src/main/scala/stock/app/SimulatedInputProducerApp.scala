package stock.app

import java.time.{LocalDateTime, ZoneOffset}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import stock._
import stock.json.JsonSerializer

import scala.math._
import scala.util.Random

object SimulatedInputProducerApp extends App {
  @volatile
  private var stop = false

  private val rnd = new Random(new java.util.Random())

  sys.addShutdownHook {
    stop = true
  }

  val symbols = SymbolsInventory.symbols

  def genTrade(symbolInfo: SymbolInfo, dt: LocalDateTime): Trade = Trade(
    symbolInfo.symbol,
    dt,
    max(
      // dummy new price generator
      symbolInfo.last
      + sin(toRadians(dt.toEpochSecond(ZoneOffset.UTC) % 360)) * max(symbolInfo.last * 0.05, 1)
      + sin(toRadians((dt.toEpochSecond(ZoneOffset.UTC) / 30) % 360)) * max(symbolInfo.last * 0.1, 1)
      + rnd.nextDouble() * 0.1 * (if (rnd.nextBoolean()) 1 else -1),
      0.01),
    rnd.between(10, 1000))

  val props = PropertiesFactory.createDefault()
  props.put("key.serializer", classOf[StringSerializer].getTypeName)
  props.put("value.serializer", classOf[JsonSerializer[Trade]].getTypeName)

  val producer = new KafkaProducer[String, Trade](props)

  try {
    while (!stop) {
      for (symbolInfo <- symbols) {
        val ts = LocalDateTime.now()
        val trade = genTrade(symbolInfo, ts)
        val record = new ProducerRecord[String, Trade](Topics.trades,
          null,
          ts.toInstant(ZoneOffset.UTC).toEpochMilli,
          null,
          trade)

        producer.send(record)
      }
      Thread.sleep(100)
    }
  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    producer.close()
  }
}
