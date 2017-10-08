package net.funfunnet.crawler.actor

import akka.actor.{Actor, ActorLogging}

class Processor extends Actor with ActorLogging{

  override def receive: Receive = {
    case msg => log.info(s"start crawling by $msg")
  }
}
