package stock.json

import java.util

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class JsonSerde[T >: Null <: Any : Manifest] extends Serde[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def serializer(): Serializer[T] = new JsonSerializer[T]

  override def deserializer(): Deserializer[T] = new JsonDeserializer[T]

  override def close(): Unit = ()
}
