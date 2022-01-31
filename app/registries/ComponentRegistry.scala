package registries

import dao.UrlMappingDaoComponent
import play.api.db.slick.DatabaseConfigProvider
import services.ShortenerServiceComponent

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ComponentRegistry @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends ShortenerServiceComponent with UrlMappingDaoComponent {
  override val urlMappingDao = new UrlMappingDao(dbConfigProvider)
  override val shortenerService = new Shortener()
}
