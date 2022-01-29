package controllers

import javax.inject._
import play.api.libs.json.{JsDefined, JsUndefined, JsValue, Json}
import play.api.mvc._
import service.Shortener

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ShortenerController @Inject()(val controllerComponents: ControllerComponents, val shortener: Shortener) extends BaseController {


  def shortenUrl(): Action[AnyContent] = Action.async { request: Request[AnyContent] =>
    val body: AnyContent          = request.body
    val jsonBody: Option[JsValue] = body.asJson

    jsonBody
      .map { json =>
        json \ "long_url" match {
          case JsDefined(value) => {
            val longUrl = value.as[String]
            shortener.shortenUrl(longUrl) map { shortUrl =>
              Ok(Json.toJson(shortUrl))
            }
          }
          case _ => Future { BadRequest("Expecting long_url") }
        }
      }
      .getOrElse {
        Future { BadRequest("Expecting application/json request body")}
      }
  }

  def redirect(shortUrl: String): Action[AnyContent] = Action.async {
    shortener.redirect(shortUrl) map {
      case Some(value) => Redirect(value.longUrl, 302)
      case None => NotFound // TODO: static 404 page maybe
    }
  }
}
