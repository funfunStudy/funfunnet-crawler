package net.funfunnet.crawler.model

import net.funfunnet.crawler.model.Site.Site

case class SiteSource(id: Int, name: String, site: Site, url: String)

object SiteSource {

  def findAll(): List[SiteSource] = {
    //TODO db에서 가져오도록 변경
    List(
      SiteSource(id = 1, name = "Rainist Engineering", site = Site.Medium,
                 url = "https://medium.com/rainist-engineering/latest"),
      SiteSource(id = 2, name = "디지털 세상을 만드는 아날로거", site = Site.Medium,
                 url = "https://medium.com/@goinhacker/latest"),
      SiteSource(id = 3, name = "Lazysoul", site = Site.Medium,
                 url = "https://medium.com/@lazysoul/latest")
    )
  }
}
