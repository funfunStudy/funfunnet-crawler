package net.funfunnet.crawler

import akka.actor.{ActorSystem, Props}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import net.funfunnet.crawler.actor.{Start, Supervisor}

object Application extends App {

  val system = ActorSystem("funfunnet-crawler")
  val supervisor = system.actorOf(Props[Supervisor], name = "supervisor")

  //call by quartz
  QuartzSchedulerExtension(system).schedule("Crawling", supervisor, Start)
}
