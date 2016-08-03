import com.marionete.stock.domain.Stock
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by matheus on 31-07-2016.
  */
class StockTest extends FlatSpec with Matchers {
  "Stock" should "be a stock instance class" in {
    Stock("APPL", 1.0, 1.0) shouldBe an[Stock]
  }
  it should "returns its name" in {
    pending
  }

  it should "returns its buying price" in {
    pending
  }

  it should "returns its asking price" in {
    pending
  }
}
