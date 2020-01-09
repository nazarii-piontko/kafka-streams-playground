package stock

import kantan.csv._
import kantan.csv.ops._

import scala.util.Using

object SymbolsInventory {
  private lazy val symbolsCache: Vector[SymbolInfo] = _loadSymbols()

  implicit private val rowDecoder: RowDecoder[SymbolInfo] = RowDecoder.ordered {
    (sym: String, name: String, price: Double, m: Double, sec: String, ind: String) =>
      SymbolInfo(sym, name, price, m, sec, ind)
  }

  private def _loadSymbols(): Vector[SymbolInfo] = {
    Using(scala.io.Source.fromResource("symbols.csv")) {
      source =>
        source.mkString
          .asCsvReader[SymbolInfo](rfc.withHeader)
          .filter(_.isRight)
          .map(_.right.get)
          .toVector
    }.get
  }

  def symbols: Vector[SymbolInfo] = symbolsCache
}
