package stock.json

import java.time.LocalDateTime

import com.google.gson.{Gson, GsonBuilder}

object GsonFactory {
  def create(): Gson = new GsonBuilder()
    .setPrettyPrinting()
    .registerTypeAdapter(classOf[LocalDateTime], new LocalDateTimeAdapter())
    .create()
}
