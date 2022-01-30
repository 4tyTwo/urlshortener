import models.UrlMapping
import org.mockito.ArgumentMatchers.{anyString, eq}
import org.mockito.MockitoSugar.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import dao.UrlMappingDaoComponent
import org.mockito.ArgumentMatchers
import org.scalatest.concurrent.ScalaFutures.whenReady
import service.ShortenerServiceComponent

trait InsertNewTestEnv extends ShortenerServiceComponent with UrlMappingDaoComponent with MockitoSugar {
  val mockDAO: UrlMappingDao = mock[UrlMappingDao]
  when(mockDAO.insert(anyString())).thenReturn( Future {UrlMapping(1, "test", null)})
  when(mockDAO.getByLongUrl(anyString())).thenReturn( Future { None })
  override val urlMappingDao: UrlMappingDao = mockDAO
  override val shortenerService: Shortener = new Shortener()
}

class ShortenerServiceTest1 extends PlaySpec with InsertNewTestEnv {

  "ShortenerService#create" should {
    "return a short url for a valid long one" in {
      val actual = shortenerService.create("https://testurl")
      whenReady(actual) { _ mustBe "b"}
    }
  }
}

trait InsertAgainTestEnv extends ShortenerServiceComponent with UrlMappingDaoComponent with MockitoSugar {
  val mockDAO: UrlMappingDao = mock[UrlMappingDao]
  when(mockDAO.getByLongUrl("https://testurl")).thenReturn( Future {Some(UrlMapping(1, "https://testurl", null))})
  override val urlMappingDao: UrlMappingDao = mockDAO
  override val shortenerService: Shortener = new Shortener()
}

class ShortenerServiceTest2 extends PlaySpec with InsertAgainTestEnv {
  "ShortenerService#create" should {
    "return the stored value if it is present" in {
      val actual = shortenerService.create("https://testurl")
      whenReady(actual) { _ mustBe "b"}
    }
  }
}

trait LookUpTestEnv extends ShortenerServiceComponent with UrlMappingDaoComponent with MockitoSugar {
  val mockDAO: UrlMappingDao = mock[UrlMappingDao]
  when(mockDAO.getById(1)).thenReturn( Future {Some(UrlMapping(1, "https://testurl", null))})
  override val urlMappingDao: UrlMappingDao = mockDAO
  override val shortenerService: Shortener = new Shortener()
}

class ShortenerServiceTest3 extends PlaySpec with LookUpTestEnv {
  "ShortenerService#lookup" should {
    "return transform short url and perform lookup by id" in {
      val actual = shortenerService.lookup("b")
      whenReady(actual) { _ mustBe Some(UrlMapping(1, "https://testurl", null))}
    }
  }
}
