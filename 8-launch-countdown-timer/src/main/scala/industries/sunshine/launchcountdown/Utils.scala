package industries.sunshine.launchcountdown

import java.time.LocalDateTime
import com.raquo.airstream.core.EventStream
import java.time.Duration

object Utils {

  def diffNowToTarget(countDownTo: LocalDateTime) = EventStream
    .periodic(11)
    .map(_ => {
      val now = LocalDateTime.now()
      Duration.between(now, countDownTo)
    })
    .map(dur => if (dur.isNegative()) Duration.ZERO else dur)
    .toSignal(Duration.ZERO)

  def isoStringToInstant(dateString: String): Option[LocalDateTime] = {
    import java.time.LocalDateTime
    import java.time.format.DateTimeFormatter
    import scala.util.Try

    val formatter: DateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val localDateTime: Option[LocalDateTime] =
      Try(LocalDateTime.parse(dateString, formatter)).toOption
    localDateTime
  }
}
