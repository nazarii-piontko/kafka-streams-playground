package stock.json

import java.util

import org.apache.kafka.common.serialization.Deserializer

import scala.reflect._

class JsonDeserializer[T >: Null <: Any : ClassTag] extends Deserializer[T] {
  private val gson = GsonFactory.create()

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def deserialize(topic: String, data: Array[Byte]): T = {
    gson.fromJson(new String(data), classTag[T].runtimeClass)
  }

  override def close(): Unit = ()
}
