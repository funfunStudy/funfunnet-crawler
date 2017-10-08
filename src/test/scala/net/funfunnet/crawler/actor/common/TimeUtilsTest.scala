package net.funfunnet.crawler.actor.common

import net.funfunnet.crawler.common.TimeUtils
import org.scalatest.FunSuite

class TimeUtilsTest extends FunSuite {
  test("parseIsoTime") {
    val dateText = "2017-09-15T06:09:11.760Z"
    val localDateTime = TimeUtils.parseIsoTime(dateText)
    assertResult(2017)(localDateTime.getYear)
    assertResult(9)(localDateTime.getMonthValue)
    assertResult(15)(localDateTime.getDayOfMonth)

    assertResult(6)(localDateTime.getHour)
    assertResult(9)(localDateTime.getMinute)
    assertResult(11)(localDateTime.getSecond)
  }
}
