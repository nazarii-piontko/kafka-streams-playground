package stock

import java.time.LocalDateTime

case class SymbolInfo(symbol: String, name: String, last: Double, marketCap: Double, sector: String, industry: String)

case class Trade(symbol: String, timestamp: LocalDateTime, price: Double, size: Long)

case class Bar(timestamp: LocalDateTime, open: Double, high: Double, low: Double, close: Double, volume: Long)

case class BarInCalc(bar: Bar, completed: Boolean)
