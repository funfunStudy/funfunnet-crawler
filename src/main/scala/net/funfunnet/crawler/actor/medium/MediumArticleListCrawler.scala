package net.funfunnet.crawler.actor.medium

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.pattern.pipe
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import net.funfunnet.crawler.model.SiteSource

class MediumArticleListCrawler extends Actor with ActorLogging {

  import context.dispatcher

  private val MEDIUMID_REGEX = "https:\\/\\/medium.com\\/(.{1,20})\\/latest".r
  private val UNIQUESLUG_REGEX = "\"uniqueSlug\":\"(.{1,100})\",\"previewContent\"".r
  private val http = Http(context.system)
  private val articleCrawler =
    context.system.actorOf(Props[MediumArticleCrawler], name = "mediumArticleCrawler")

  final implicit val materializer: ActorMaterializer =
    ActorMaterializer(ActorMaterializerSettings(context.system))

  override def receive: Receive = {
    case source: SiteSource =>
      http.singleRequest(HttpRequest(uri = source.url)).pipeTo(self)(sender())

    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      log.info("response ok")
      val sd = sender()
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        log.info(s"Got response, body length: ${body.length}")
        findArticleUrls(body.utf8String).foreach(x => {
          articleCrawler.tell(x, sd)
        })
      }
      log.info("end of response ok")
    case resp@HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()
  }

  def findPrefixUrl(html: String): Option[String] = MEDIUMID_REGEX.findFirstMatchIn(html)
    .map(x => x.group(1))
    .map(x => s"https://medium.com/$x")

  def findUniqueSlugs(html: String): List[String] =
    UNIQUESLUG_REGEX.findAllMatchIn(html).map(x => x.group(1)).toList

  def findArticleUrls(html: String): List[String] =
    findPrefixUrl(html).map(x => findUniqueSlugs(html).map(y => s"$x/$y"))
      .getOrElse(Nil)
}
