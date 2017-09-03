
import dal.ProductRepository
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class ModelSpec extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures {
  import models._

  import scala.concurrent.ExecutionContext.Implicits.global

  def productService: ProductRepository = app.injector.instanceOf(classOf[ProductRepository])

  "Products" should {

    "be listed" in {
      whenReady(productService.list()) { products =>
        products.length must equal(2)
      }
    }

    "be searched by name" in {
      whenReady(productService.search(10, "superstar", "name", 0, "name", "asc")) { products =>
        products.length must equal(1)
      }
    }
  }
}
