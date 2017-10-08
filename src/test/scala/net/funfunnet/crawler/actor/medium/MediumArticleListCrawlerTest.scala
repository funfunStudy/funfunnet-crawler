package net.funfunnet.crawler.actor.medium

import java.net.URLEncoder

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.typesafe.scalalogging.LazyLogging
import org.scalatest._

import scala.io.Source

class MediumArticleListCrawlerTest extends TestKit(ActorSystem("MediumArticleListCrawlerTest"))
                                           with ImplicitSender
                                           with FunSuiteLike
                                           with Matchers
                                           with BeforeAndAfterAll
                                           with LazyLogging {

  lazy val html = Source.fromURL(getClass.getClassLoader.getResource("crawler/medium/latest.html"))
    .getLines().mkString

  lazy val crawler = TestActorRef(new MediumArticleListCrawler()).underlyingActor

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  test("findPrefixUrl") {
    assertResult(Some("https://medium.com/rainist-engineering")) {
      crawler.findPrefixUrl(html)
    }
  }

  test("findUniqueSlugs") {
    val slugs = crawler.findUniqueSlugs(html)
    assertResult(7)(slugs.size)
    assertResult("writing-aws-lambda-function-in-kotlin-b3faf3f55777")(slugs.head)
    assertResult("레이니스트의-기술-블로그를-시작하며-2d757ea69844")(slugs.last)
  }

  test("findArticleUrls") {
    val urls = crawler.findArticleUrls(html)
    assertResult(7)(urls.size)
    assertResult("https://medium.com/rainist-engineering/writing-aws-lambda-function-in-kotlin-b3faf3f55777")(urls.head)
    assertResult("https://medium.com/rainist-engineering/레이니스트의-기술-블로그를-시작하며-2d757ea69844")(urls.last)
  }
}
