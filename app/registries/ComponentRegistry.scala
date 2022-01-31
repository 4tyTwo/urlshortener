package registries

import dao.UrlMappingDaoComponent
import play.api.db.slick.DatabaseConfigProvider
import services.ShortenerServiceComponent

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

class ComponentRegistry @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends ShortenerServiceComponent with UrlMappingDaoComponent {
  override val urlMappingDao = new UrlMappingDao(dbConfigProvider)
  override val shortenerService = new Shortener()
}
