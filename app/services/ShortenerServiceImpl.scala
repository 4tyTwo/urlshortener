package services

import models.UrlMapping
import repos.UrlMappingRepo

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ShortenerServiceImpl @Inject() (val urlMappingRepo: UrlMappingRepo)(implicit ec: ExecutionContext) extends ShortenerService {

  override def lookup(shortUrl: String): Future[Option[UrlMapping]] =
    urlMappingRepo.getById(B52Converter.decode(shortUrl))

  override def create(longUrl: String): Future[String] =
    urlMappingRepo.getByLongUrl(longUrl) flatMap {
      case Some(value) => Future { B52Converter.encode(value.id) }
      case None => shortenUrl(longUrl)
    }

  private def shortenUrl(longUrl: String): Future[String] = {
    urlMappingRepo.insert(longUrl) flatMap {
      mapping => Future {B52Converter.encode(mapping.id)}
    }
  }

  private object B52Converter {

    val base = 52
    val alphabet: Seq[Char] = (('a' to 'z') ++ ('A' to 'Z')).toList

    def encode(long: Long): String = {
      doEncode(long).reverse.mkString
    }

    // TODO: tailrec
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
