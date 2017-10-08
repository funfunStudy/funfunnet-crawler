package net.funfunnet.crawler.common

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZonedDateTime}

object TimeUtils {
  def parseIsoTime(text: String): LocalDateTime =
    ZonedDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME).toLocalDateTime
}
