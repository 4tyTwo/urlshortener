package repos

import models.UrlMapping
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.sql.Timestamp
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UrlMappingRepoPg @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with UrlMappingRepo {
  import profile.api._
  private val urlMappings = TableQuery[UrlMappingTable]

  override def getByLongUrl(longUrl: String):Future[Option[UrlMapping]] = {
    val q = urlMappings.filter {_.longUrl === longUrl}
    db.run(q.result.headOption)
  }

  override def insert(longUrl: String): Future[UrlMapping] =
    db.run {
      (urlMappings.map {_.longUrl} returning urlMappings) += longUrl
    }

  override def getById(id: Long): Future[Option[UrlMapping]] = {
    val q = urlMappings.filter {_.id === id}
    db.run(q.result.headOption)
  }


  private class UrlMappingTable(tag: Tag) extends Table[UrlMapping](tag, "url_mapping_v0") {

    def longUrl: Rep[String] = column[String]("long_url")
    def id: Rep[Long] = column[Long]("id")
    def created_at: Rep[Timestamp] = column[Timestamp]("created_at")

    def * = (id, longUrl, created_at) <> (UrlMapping.tupled, UrlMapping.unapply)
  }
}