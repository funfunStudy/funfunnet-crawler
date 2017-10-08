package net.funfunnet.crawler.actor.medium

import java.net.URLEncoder
import java.time.{LocalDateTime, ZonedDateTime}
import java.time.format.DateTimeFormatter

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes, Uri}
import akka.pattern.pipe
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import net.funfunnet.crawler.actor.Result
import net.funfunnet.crawler.common.TimeUtils
import net.funfunnet.crawler.model.Article
import org.jsoup.Jsoup

class MediumArticleCrawler extends Actor with ActorLogging {

  import context.dispatcher

  private val DATE_REGEX = "\"datePublished\":\"(.{1,30})\",\"dateModified".r

  private val http = Http(context.system)

  final implicit val materializer: ActorMaterializer =
    ActorMaterializer(ActorMaterializerSettings(context.system))

  override def receive: Receive = {
    case url: String =>
      http.singleRequest(HttpRequest(uri = encodeUrl(url))).pipeTo(self)(sender())

    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      val sd = sender()
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        sd ! Result(findArticle(body.utf8String))
      }
    case resp@HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()
  }

  def encodeUrl(url: String) : String = {
    val prefix = url.substring(0, url.lastIndexOf("/") + 1)
    val params = url.substring(url.lastIndexOf("/") + 1)
    prefix + URLEncoder.encode(params, "UTF-8")
  }

  def findArticle(html: String): Article = {
    val doc = Jsoup.parse(html)
    val title = doc.select("meta[property=og:title]").attr("content")
    val desc = doc.select("meta[property=og:description]").attr("content")
    val url = doc.select("meta[property=og:url]").attr("content")
    val image = doc.select("meta[property=og:image]").attr("content")

    val dateStr = DATE_REGEX.findFirstMatchIn(html).map(x => x.group(1)).get
    val createdAt = TimeUtils.parseIsoTime(dateStr)

    Article(title, desc, image, url, createdAt)
  }
}
