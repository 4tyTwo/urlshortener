package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class ShortenerController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson("Hello World"))
  }
}
