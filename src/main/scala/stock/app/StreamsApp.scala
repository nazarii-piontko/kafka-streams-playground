package stock.app

import cask.model.Response
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import scalaj.http.Http
import stock.json.GsonFactory
import stock.{PropertiesFactory, TopologyBuilder, Trade}

import scala.reflect.classTag

object StreamsApp extends cask.MainRoutes {
  private val gson = GsonFactory.create()

  private val serviceHost = "127.0.0.1"
  private val servicePort = if (sys.env.contains("PORT")) sys.env("PORT").toInt else 5000
  private val serviceAddress = serviceHost + ":" + servicePort.toString

  private val props = PropertiesFactory.createDefault()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stock")
  props.put(StreamsConfig.APPLICATION_SERVER_CONFIG, serviceAddress)
  props.put(StreamsConfig.STATE_DIR_CONFIG, s"./data/stock-app-$servicePort")

  private val topology = TopologyBuilder.build()

  private val streams: KafkaStreams = new KafkaStreams(topology, props)
  streams.start()

  @cask.get("/:symbol/last")
  def getLast(symbol: String): Response[String] = {
    val metaForKey = streams.metadataForKey(stock.Stores.lastTrade, symbol, new StringSerializer)
    val trade = if (metaForKey.host == serviceHost && metaForKey.port() == port) {
      val store = streams.store(stock.Stores.lastTrade, QueryableStoreTypes.keyValueStore[String, Trade]())
      store.get(symbol)
    } else {
      val response = Http(s"http://${metaForKey.host()}:${metaForKey.port()}/$symbol/last").asString
      gson.fromJson[Trade](response.body, classTag[Trade].runtimeClass)
    }
    cask.Response(
      gson.toJson(trade),
      headers = Seq("Content-Type" -> "application/json"))
  }

  override def host: String = "0.0.0.0"

  override def port: Int = servicePort

  initialize()
}