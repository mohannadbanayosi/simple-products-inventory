package routers

import javax.inject.Inject

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._
import play.api.Logger

import models._
import dal._
import controllers._

/**
  * Routes and URLs to the ProductResource controller.
  */
class ProductRouter @Inject()(controller: ProductController) extends SimpleRouter {

  // TODO: Use the class logger instead.
  // private val logger = Logger(this.getClass)

  override def routes: Routes = {
    case GET(p"/browse" ? q_?"per_page=${ int(per_page) }" & q_?"q=$q" & q_?"c=$c" & q_?"page=${ int(page) }" & q_?"sort=$sort" & q_?"direction=$direction") =>
      Logger.info("GET: browse products")
      controller.getProducts(per_page, q, c, page, sort, direction)

    case POST(p"/add") =>
      Logger.info("POST: add a new product")
      controller.addProduct
  }
}
