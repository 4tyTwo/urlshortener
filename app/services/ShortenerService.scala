package services

import models.UrlMapping

import scala.concurrent.Future

trait ShortenerService {
  def lookup(shortUrl: String): Future[Option[UrlMapping]]
  def create(longUrl: String): Future[String]
}