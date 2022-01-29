package dao

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import models.UrlMapping
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Timestamp

class UrlMappingDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._
  private val urlMappings = TableQuery[UrlMappingTable]

  def getByLongUrl(longUrl: String):Future[Option[UrlMapping]] = {
    val q = urlMappings.filter(_.longUrl === longUrl)
    db.run(q.result.headOption)
  }

  def insert(longUrl: String): Future[UrlMapping] = {
    db.run {
      (urlMappings.map(_.longUrl) returning urlMappings) += longUrl
    }
  }

  def getById(id: Long): Future[Option[UrlMapping]] = {
    val q = urlMappings.filter(_.id === id)
    db.run(q.result.headOption)
  }


    private class UrlMappingTable(tag: Tag) extends Table[UrlMapping](tag, "url_mapping_v0") {

    def longUrl = column[String]("long_url")
    def id = column[Long]("id")
    def created_at = column[Timestamp]("created_at")

    def * = (id, longUrl, created_at) <> (UrlMapping.tupled, UrlMapping.unapply)
  }
}
