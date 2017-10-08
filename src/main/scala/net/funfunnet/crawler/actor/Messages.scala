package net.funfunnet.crawler.actor

import net.funfunnet.crawler.model.{Article, SiteSource}

case class Start()
case class Crawl(siteSource: SiteSource)
case class Result(article: Article)
