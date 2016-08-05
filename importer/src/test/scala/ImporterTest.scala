import com.marionete.stock.domain.Stock
import com.marionete.stock.importer._
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by matheus on 31-07-2016.
  */
class ImporterTest extends FlatSpec with Matchers {
  "Yahoo Finance API" should "be able to import stocks from yahoo" in{
    Yahoo.getStocks(Seq("AAPL","YHOO")) shouldBe an [Seq[Stock]]
  }
}
