package stock.json

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.google.gson.TypeAdapter
import com.google.gson.stream.{JsonReader, JsonWriter}

class LocalDateTimeAdapter extends TypeAdapter[LocalDateTime] {
  override def write(out: JsonWriter, dateTime: LocalDateTime): Unit =
    out.value(dateTime.format(DateTimeFormatter.ISO_DATE_TIME))

  override def read(in: JsonReader): LocalDateTime =
    LocalDateTime.parse(in.nextString(), DateTimeFormatter.ISO_DATE_TIME)
}
