package net.funfunnet.crawler.model

import java.time.LocalDateTime

case class Article(
  title: String,
  desc: String,
  image: String,
  url: String,
  createdAt: LocalDateTime
)
