package controllers

import javax.inject._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import service.Shortener
import play.api.libs.json._

import java.net.{MalformedURLException, URL}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ShortenerController @Inject()(val controllerComponents: ControllerComponents, val shortener: Shortener) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[ShortenBody].fold(
      error => Future {
        val e = error.head
        val where = e._1.toString()
        val what = e._2.head.message
        BadRequest(Json.toJson(Map(where -> what)))
      },
      body => {
        shortener.create(body.longUrl) map { shortUrl =>
                        Ok(Json.toJson(Map("short_url" -> shortUrl)))
        }
      }
    )
  }

  def lookup(shortUrl: String): Action[AnyContent] = Action.async {
    shortener.lookup(shortUrl) map {
      case Some(value) => Redirect(value.longUrl, 302)
      case None => NotFound // TODO: static 404 page maybe
    }
  }
}

case class ShortenBody(longUrl: String)

object ShortenBody {
  implicit val shortenBodyReads: Reads[ShortenBody] = {
    val longUrl = (JsPath \ "long_url").read[String].filter(JsonValidationError("long_url is not a valid url"))(isValidUrl)
    longUrl.map(ShortenBody(_))
  }

  // Rudimentary, but should mimic tinyurl behaviour
  def isValidUrl(url: String) = {
    try {
      new URL(url)
      true
    }
    catch {
      case _: MalformedURLException => false
    }
  }
}
