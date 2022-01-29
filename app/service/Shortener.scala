package service

import dao.UrlMappingDao

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Shortener @Inject() (urlMappingDao: UrlMappingDao) {

  def redirect(shortUrl: String): Future[Option[String]] = {
    val id = B52Converter.decode(shortUrl)
    urlMappingDao.getById(id).flatMap {
      case Some(value) => Future { Some(value.longUrl) }
      case None => Future { None }
    }
  }


  private def shortUrlToId(shortUrl: String) = {
  }

  private object B52Converter {

    val base = 52
    val alphabet: Seq[Char] = (('a' to 'z') ++ ('A' to 'Z')).toList

    def encode(long: Long): String = {
      doEncode(long).mkString
    }

    private def doEncode(long: Long): Seq[Char] = {
      if (long < base)
        alphabet(long.toInt) :: Nil
      else {
        val rem = long / base
        val c = alphabet(rem.toInt)
        c +: encode(long % base)
      }
    }

    def decode(string: String): Long =
      string.toCharArray.foldRight((0L, 0))(
        (c, acc) => (acc._1 + alphabet.indexOf(c) * Math.pow(base, acc._2).toLong, acc._2 + 1)
      )._1 // TODO: replace indexOf
  }
}
