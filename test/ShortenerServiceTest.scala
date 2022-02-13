import repos.UrlMappingRepo
import models.UrlMapping
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockitoSugar.when
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import services.ShortenerServiceImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ShortenerServiceTest extends PlaySpec with MockitoSugar {

  "ShortenerService#create" should {
    "return a short url for a valid long one" in {
      val mockRepo: UrlMappingRepo = mock[UrlMappingRepo]
      when(mockRepo.insert(anyString())).thenReturn( Future {UrlMapping(1, "test", null)})
      when(mockRepo.getByLongUrl(anyString())).thenReturn( Future { None })
      val actual = new ShortenerServiceImpl(mockRepo).create("https://testurl")
      whenReady(actual) { _ mustBe "b"}
    }
  }

  "ShortenerService#create" should {
    "return the stored value if it is present" in {
      val mockRepo: UrlMappingRepo = mock[UrlMappingRepo]
      when(mockRepo.getByLongUrl(anyString())).thenReturn( Future { Some { UrlMapping(2, "test", null) } })
      val actual = new ShortenerServiceImpl(mockRepo).create("https://testurl")
      whenReady(actual) { _ mustBe "c"}
    }
  }

  "ShortenerService#lookup" should {
    "return transform short url and perform lookup by id" in {
      val mockRepo: UrlMappingRepo = mock[UrlMappingRepo]
      when(mockRepo.getById(1)).thenReturn( Future {Some(UrlMapping(1, "https://testurl", null))})
      val actual = new ShortenerServiceImpl(mockRepo).lookup("b")
      whenReady(actual) { _ mustBe Some(UrlMapping(1, "https://testurl", null))}
    }
  }
}
