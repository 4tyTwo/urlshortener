package services

import dao.UrlMappingDaoComponent
import models.UrlMapping

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ShortenerServiceComponent {
  this: UrlMappingDaoComponent =>
  val shortenerService: Shortener

  class Shortener {

    def lookup(shortUrl: String): Future[Option[UrlMapping]] = {
      val id = B52Converter.decode(shortUrl)
      urlMappingDao.getById(id)
    }

    def create(longUrl: String): Future[String] = {
      urlMappingDao.getByLongUrl(longUrl) flatMap {
        case Some(value) => Future { B52Converter.encode(value.id) }
        case None => shortenUrl(longUrl)
      }
    }

    private def shortenUrl(longUrl: String): Future[String] = {
      urlMappingDao.insert(longUrl) flatMap {
        mapping => Future {B52Converter.encode(mapping.id)}
      }
    }
  }

  private object B52Converter {

    val base = 52
    val alphabet: Seq[Char] = (('a' to 'z') ++ ('A' to 'Z')).toList

    def encode(long: Long): String = {
      doEncode(long).reverse.mkString
    }

    private def doEncode(long: Long): Seq[Char] = {
      if (long < base)
        alphabet(long.toInt) :: Nil
      else {
        val rem = long % base
        val c = alphabet(rem.toInt)
        c +: doEncode(long / base)
      }
    }

    def decode(string: String): Long = {
      string.toCharArray.foldRight((0L, 0))(
        (c, acc) => (acc._1 + alphabet.indexOf(c) * Math.pow(base, acc._2).toLong, acc._2 + 1)
      )._1 // TODO: replace indexOf
    }
  }
}


