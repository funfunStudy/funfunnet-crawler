package net.funfunnet.crawler

import akka.actor.{ActorSystem, Props}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import scala.concurrent.duration._

object Application extends App {

  val system = ActorSystem("funfunnet-crawler")

  import system.dispatcher

  val crawlingBySchedulerActor = system.actorOf(Props[CrawlingActor], name = "crawlingBySchedulerActor")
  val crawlingByQuartzActor = system.actorOf(Props[CrawlingActor], name = "crawlingByQuartzActor")


  //call by scheduler
  val cancellable =
    system.scheduler.schedule(5 seconds, 10 seconds, crawlingBySchedulerActor, "scheduler")

  //call by quartz
  QuartzSchedulerExtension(system).schedule("Every30Seconds", crawlingByQuartzActor, "quartz")
}
