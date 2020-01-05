package stock

import java.time.{LocalDateTime, ZoneOffset}

import javafx.util.Duration
import org.apache.kafka.streams.kstream.Transformer
import org.apache.kafka.streams.processor.{ProcessorContext, To}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.state.{KeyValueStore, Stores}
import org.apache.kafka.streams.{KeyValue, Topology}
import stock.json.JsonSerde

import scala.math._

object TopologyBuilder {

  def build(): Topology = {

    implicit val tradeSerde: JsonSerde[Trade] = new JsonSerde[Trade]
    implicit val barSerde: JsonSerde[Bar] = new JsonSerde[Bar]

    val builder: StreamsBuilder = new StreamsBuilder

    // Prepare input stream

    val tradesStream = builder
      .stream[String, Trade](Topics.trades)
      .selectKey((_, v) => v.symbol)

    // KTable with last trade per symbol

    tradesStream
      .groupByKey
      .reduce((_, v) => v)

    // 1 Min bars calculation

    val barStoreName = "bars"
    builder.addStateStore(
      Stores.keyValueStoreBuilder(
        Stores.inMemoryKeyValueStore(barStoreName),
        Serdes.String,
        barSerde))

    val barsMin1 = tradesStream
      .transform[String, BarInCalc](() =>
        new TradeIntoBarTransformer(
          Duration.minutes(1),
          barStoreName),
        barStoreName)
      .branch(
        (_, v) => v.completed,
        (_, v) => !v.completed)

    barsMin1(0)
      .mapValues((_, v) => v.bar)
      .to("bars_min_1")

    barsMin1(1)
      .mapValues((_, v) => v.bar)
      .to("bars_min_1_ongoing")

    // Done, build & return topology

    builder.build()
  }
}

class TradeIntoBarTransformer(private val timeFrame: Duration,
                              private val storeName: String)
  extends Transformer[String, Trade, KeyValue[String, BarInCalc]] {

  private var context: ProcessorContext = _
  private var stateStore: KeyValueStore[String, Bar] = _

  override def init(context: ProcessorContext): Unit = {
    this.context = context

    stateStore = context
      .getStateStore(storeName)
      .asInstanceOf[KeyValueStore[String, Bar]]
  }

  override def transform(symbol: String, trade: Trade): KeyValue[String, BarInCalc] = {
    val ts = getBarTimestamp(trade.timestamp)

    val currBar = stateStore.get(symbol)

    val newBar = {
      if (currBar == null || currBar.timestamp != ts) {
        Bar(ts, trade.price, trade.price, trade.price, trade.price, trade.size)
      } else {
        val high = max(currBar.high, trade.price)
        val low = min(currBar.low, trade.price)
        val volume = currBar.volume + trade.size
        Bar(ts, currBar.open, high, low, trade.price, volume)
      }
    }

    stateStore.put(symbol, newBar)

    if (currBar != null && currBar.timestamp != newBar.timestamp) {
      context.forward(
        symbol,
        BarInCalc(currBar, completed = true),
        To.all()
          .withTimestamp(currBar.timestamp.toInstant(ZoneOffset.UTC).toEpochMilli))
    }

    context.forward(
      symbol,
      BarInCalc(newBar, completed = false),
      To.all()
        .withTimestamp(newBar.timestamp.toInstant(ZoneOffset.UTC).toEpochMilli))

    null
  }

  override def close(): Unit = ()

  private def getBarTimestamp(ts: LocalDateTime): LocalDateTime = {
    val tsSec = ts.toEpochSecond(ZoneOffset.UTC)
    val periodSec =  timeFrame.toSeconds.toLong
    val roundedTsSec = tsSec - tsSec % periodSec
    val roundedTs = LocalDateTime.ofEpochSecond(roundedTsSec, 0, ZoneOffset.UTC)
    roundedTs
  }
}
