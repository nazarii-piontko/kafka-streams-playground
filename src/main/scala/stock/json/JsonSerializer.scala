package stock.json

import java.nio.charset.Charset
import java.util

import org.apache.kafka.common.serialization.Serializer

class JsonSerializer[T] extends Serializer[T] {
  private val gson = GsonFactory.create()

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def serialize(topic: String, data: T): Array[Byte] =
    gson.toJson(data).getBytes(Charset.forName("utf-8"))

  override def close(): Unit = ()
}



