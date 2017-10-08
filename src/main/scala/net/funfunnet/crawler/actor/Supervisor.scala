package net.funfunnet.crawler.actor

import akka.actor.{Actor, ActorLogging, Props}
import net.funfunnet.crawler.actor.medium.MediumArticleListCrawler
import net.funfunnet.crawler.model.{Site, SiteSource}

class Supervisor extends Actor with ActorLogging {
  val actors = List(
    context.system.actorOf(Props[MediumArticleListCrawler], name = Site.Medium.toString)
  )
  val crawlers = actors.map(x => x.path.name -> x).toMap

  override def receive: Receive = {
    case Start =>
      log.info("start")
      SiteSource.findAll().foreach(self ! Crawl(_))
    case Crawl(siteSource) =>
      log.info(s"crawl to ${siteSource.name}")
      crawlers.get(siteSource.site.toString) match {
        case Some(ref) => ref ! siteSource
        case _ => log.error(s"could not found crawler actor : ${siteSource.site.toString}")
      }
    case Result(article) =>
      //TODO 저장 관련 처리
      log.info(s"result : title:${article.title}, url:${article.url}")
    case x =>
      log.warning(s"unknown message type : $x")
  }

}
