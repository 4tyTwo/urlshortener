package repos

import models.UrlMapping

import scala.concurrent.Future

trait UrlMappingRepo {
  def getByLongUrl(longUrl: String):Future[Option[UrlMapping]]
  def insert(longUrl: String): Future[UrlMapping]
  def getById(id: Long): Future[Option[UrlMapping]]
}

