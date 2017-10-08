package net.funfunnet.crawler.actor.medium

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.typesafe.scalalogging.LazyLogging
import org.scalatest._

import scala.io.Source

class MediumArticleCrawlerTest extends TestKit(ActorSystem("MediumArticleListCrawlerTest"))
                                       with ImplicitSender
                                       with FunSuiteLike
                                       with Matchers
                                       with BeforeAndAfterAll
                                       with LazyLogging {

  lazy val html = Source.fromURL(getClass.getClassLoader.getResource("crawler/medium/article.html"))
    .getLines().mkString

  lazy val crawler = TestActorRef(new MediumArticleCrawler()).underlyingActor

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  test("encodeUrl") {
    val url = "https://medium.com/rainist-engineering/레이니스트의-기술-블로그를-시작하며-2d757ea69844"
    val encoded = "https://medium.com/rainist-engineering/" +
                  "%EB%A0%88%EC%9D%B4%EB%8B%88%EC%8A%A4%ED%8A%B8%EC%9D%98-%EA%B8%B0%EC%88%A0-" +
                  "%EB%B8%94%EB%A1%9C%EA%B7%B8%EB%A5%BC-%EC%8B%9C%EC%9E%91%ED%95%98%EB%A9%B0-2d757ea69844"
    assertResult(encoded)(crawler.encodeUrl(url))
  }

  test("findArticle") {
    val article = crawler.findArticle(html)
    assertResult("Kotlin, AWS 그리고 레이니스트와 함께라면 육군훈련소에서도 외롭지 않아 – Rainist Engineering – Medium") {
      article.title
    }
    assertResult("Kotlin과 AWS를 활용해 간단한 Slack 봇을 만든 경험을 통해 뱅크샐러드 Android 앱의 Architecture를 간단히 공유합니다.") {
      article.desc
    }
    assertResult("https://cdn-images-1.medium.com/max/1200/1*KIa4eZARMVelBWNRMd3D9Q.png") {
      article.image
    }
    assertResult("https://medium.com/rainist-engineering/writing-aws-lambda-function-in-kotlin-b3faf3f55777") {
      article.url
    }

    assertResult(6)(article.createdAt.getHour)
    assertResult(9)(article.createdAt.getMinute)
    assertResult(11)(article.createdAt.getSecond)
  }

}
