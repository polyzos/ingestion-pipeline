import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

import thrift.logschema.{DeviceType, LogEvent, LogType}
import scala.util.Random

/**
  * Randomly generates log events
  * */
object LogEventGenerator {

  // a list with pollsters
  private lazy val pollsters = Set(
    "Public Policy Polling",
    "YouGov",
    "Qunnipiac",
    "IPSOS",
    "Morning Consult",
    "Kaiser Family Foundation",
    "Monmouth",
    "NBC/WSJ"
  )

  // a map of pollsters and their urls
  private lazy  val pollsterUrls = Map(
    "Public Policy Polling" -> "http://www.publicpolicypolling.com/pdf/2017/PPP_Release_National_61217.pdf",
    "YouGov" -> "https://d25d2506sfb94s.cloudfront.net/cumulus_uploads/document/4vf8rhr0jz/econToplines.pdf",
    "Qunnipiac" -> "https://poll.qu.edu/national/release-detail?ReleaseID=2463",
    "IPSOS" -> "https://www.realclearpolitics.com/docs/Core_Political-Topline-2017-05-31.pdf",
    "Morning Consult" -> "https://morningconsult.com/wp-content/uploads/2017/05/170516_crosstabs_Politico_v3_TB-1.pdf",
    "Kaiser Family Foundation" -> "http://files.kff.org/attachment/Topline-Kaiser-Health-Tracking-Poll-May-2017-The-AHCAs-Proposed-Changes-to-Health-Care",
    "Monmouth" -> "https://www.monmouth.edu/polling-institute/reports/MonmouthPoll_US_052417/",
    "NBC/WSJ" -> "https://www.scribd.com/document/348302118/NBC-WSJ-May-2017-poll"
  )

  private lazy  val schemaVersion = 1.toShort

  private lazy  val random = new Random()

  /**
    * Randomly generates an event
    * */
  def generateLogEvent(): LogEvent = {
    val pollster = pollsters.toSeq(random.nextInt(pollsters.size))
    val pollsterUrl = pollsterUrls.getOrElse(pollster, "")
    val total = random.nextInt(480) + 20
    val favor = random.nextInt(total)
    val opposite = total - favor
    val (start, end) = getStartEndDates()

    new LogEvent(
      schemaVersion,
      new Date().toString,
      start,
      end,
      pollster,
      favor,
      opposite,
      total,
      pollsterUrl,
      DeviceType.WEBAPP,
      LogType.INFO
    )
  }

  /**
    * Creates random dates for the start and end of a poll
    * the dates are in the format dd/mm/yyyy
    * */
  private def getStartEndDates(): (String, String) = {
    val year = random.nextInt(9) + 2010
    val month = {
      val m = random.nextInt(12) + 1
      if (m < 10) s"0$m" else m.toString
    }
    val day =  {
      if (month.equals("02")) {
        val d = random.nextInt(28) + 1
        if (d < 10) s"0$d" else d
      }
      else {
        val d = random.nextInt(30) + 1
        if (d < 10) s"0$d" else d
      }
    }

    val date = LocalDate.parse(s"$year-$month-$day")
    val start = DateTimeFormatter
      .ofPattern("dd/MM/YYYY")
      .format(date)

    val end = DateTimeFormatter
      .ofPattern("dd/MM/YYYY")
      .format(date.plusMonths(random.nextInt(12)).plusDays(random.nextInt(30)))

    (start, end)
  }
}
