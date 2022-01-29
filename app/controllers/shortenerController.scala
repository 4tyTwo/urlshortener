package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import service.Shortener
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ShortenerController @Inject()(val controllerComponents: ControllerComponents, val shortener: Shortener) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson("Hello World"))
  }

  def redirect(shortUrl: String): Action[AnyContent] = Action.async {
    shortener.redirect(shortUrl) map {
      case Some(value) => Redirect(value.longUrl, 302)
      case None => NotFound // TODO: static 404 page maybe
    }
  }
}
