package net.funfunnet.crawler

import akka.actor.{Actor, ActorLogging}

class CrawlingActor extends Actor with ActorLogging{

  override def receive: Receive = {
    case msg => log.info(s"start crawling by $msg")
  }
}
