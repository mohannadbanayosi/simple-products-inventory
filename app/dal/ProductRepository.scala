package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.Product

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for products.
 *
 * @param dbConfigProvider The Play db config provider.
 */
@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  /**
   * Product table definition.
   */
  private class ProductTable(tag: Tag) extends Table[Product](tag, "products") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def brand = column[String]("brand")

    def imageUrl = column[String]("image_url")

    def price = column[BigDecimal]("price")

    /**
     * Table's default "projection".
     */
    def * = (id, name, brand, imageUrl, price) <> ((Product.apply _).tupled, Product.unapply)
  }

  private val products = TableQuery[ProductTable]

  /**
   * Creates a new product entry.
   */
  def create(name: String, brand: String, imageUrl: String, price: BigDecimal): Future[Product] = db.run {
    (products.map(p => (p.name, p.brand, p.imageUrl, p.price))
      returning products.map(_.id)
      into ((info, id) => Product(id, info._1, info._2, info._3, info._4))
    ) += (name, brand, imageUrl, price)
  }

  /**
   * List all the products in the database.
   */
  def list(): Future[Seq[Product]] = db.run {
    products.result
  }

  /**
   * Perform search on the products table.
   */
  def search(perPage: Int, q: String, c: String, page: Int, sort: String, direction: String): Future[Seq[Product]] =  {
    // TODO: Find a better way to start the query rather than sorting it by id (which it is already).
    var query = products.sortBy(_.id)

    c match {
      case "brand" => query = query.filter(_.brand.toLowerCase.like(s"%$q%"))
      case _ => query = query.filter(_.name.toLowerCase.like(s"%$q%"))
    }

    sort match {
      case "name" => query = if (direction=="desc") query.sortBy(_.name.desc) else query.sortBy(_.name.asc)
      case "brand" => query = if (direction=="desc") query.sortBy(_.brand.desc) else query.sortBy(_.brand.asc)
      case _ => query = if (direction=="desc") query.sortBy(_.price.desc) else query.sortBy(_.price.asc)
    }

    query = query.drop(page*perPage).take(perPage)

    db.run(query.result)
  }
}
