package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.Logger
import models._
import dal._

import scala.concurrent.{ ExecutionContext, Future }

import javax.inject._

class ProductController @Inject() (repo: ProductRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  /**
   * The mapping for the product form.
   */
  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "brand" -> nonEmptyText,
      "imageUrl" -> nonEmptyText,
      "price" -> number.verifying(min(0), max(10000))
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  private val logger = Logger(getClass)

  def index = Action {
    Ok(views.html.index_products(productForm))
  }

  /**
   * The add product action.
   */
  def addProduct = Action.async { implicit request =>
    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index_products(errorForm)))
      },
      product => {
        repo.create(product.name, product.brand, product.imageUrl, product.price).map { _ =>
          Redirect(routes.ProductController.index)
        }
      }
    )
  }

  /**
   * The endpoint to get the products and performs the search too.
   */
  def getProducts(per_page: Option[Int], q: Option[String], c: Option[String], page: Option[Int], sort: Option[String], direction: Option[String]) = Action.async {
  	repo.search(per_page.getOrElse(10), q.getOrElse("").toLowerCase, c.getOrElse(""), page.getOrElse(0), sort.getOrElse("price"), direction.getOrElse("asc")).map { product =>
      Ok(Json.toJson(product))
    }
  }
}

/**
 * The create product form.
 */
case class CreateProductForm(name: String, brand: String, imageUrl: String, price: Int)
