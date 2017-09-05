package models

import play.api.libs.json._

case class Product(id: Long, name: String, brand: String, imageUrl: String, price: BigDecimal)

object Product {
  
  implicit val productFormat = Json.format[Product]
}
