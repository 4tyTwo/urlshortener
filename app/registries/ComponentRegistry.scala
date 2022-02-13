package registries

import repos.UrlMappingRepoPg
import play.api.db.slick.DatabaseConfigProvider
import services.ShortenerServiceImpl

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ComponentRegistry @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  private val urlMappingRepo: repos.UrlMappingRepo = new UrlMappingRepoPg(dbConfigProvider)
  val shortenerService = new ShortenerServiceImpl(urlMappingRepo)
}
