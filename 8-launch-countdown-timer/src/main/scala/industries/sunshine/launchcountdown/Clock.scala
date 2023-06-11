package industries.sunshine.launchcountdown

import com.raquo.laminar.api.L.{*, given}
import java.time.{Instant, LocalDateTime, Period}
import java.time.Duration
import java.time.temporal.TemporalUnit

object Clock {
  def render(countDownTo: LocalDateTime) = {
    val diffNowToTarget = EventStream
      .periodic(11)
      .map(_ => {
        val now = LocalDateTime.now()
        Duration.between(now, countDownTo)
      })
      .toSignal(Duration.ZERO)

    div(
      className := "px-8 w-full text-neutral-desaturated-blue",
      className := "grid grid-cols-4 justify-between",
      renderFlipper(diffNowToTarget.map(_.toDays().toInt), "Days"),
      renderFlipper(diffNowToTarget.map(_.toHoursPart().toInt), "Hours"),
      renderFlipper(diffNowToTarget.map(_.toMinutesPart().toInt), "Minutes"),
      renderFlipper(diffNowToTarget.map(_.toSecondsPart().toInt), "Seconds")
    )
  }

  private def renderFlipper(timePart: Signal[Int], unit: String) = {
    div(
      className := "grid justify-items-center grid-rows-[repeat(2,_60px)]",
      // className := "grid grid-rows-2 items-center",
      div(
        className := "grid relative w-[60px] h-[20px] grid-rows-[repeat(2,_30px)]",
        div(
          className := "rounded-md bg-clock-top"
          // className := "h-1/2 bg-neutral-dark-blue"
        ),
        div(
          className := "",
          className := "rounded-md bg-clock-bottom"
          // className := "bg-neutral-desaturated-blue"
        ),
        div(
          className := "absolute row-span-2 row-start-1 place-self-center text-4xl text-primary-red",
          child.text <-- timePart.map(num => "%02d".format(num))
        )
      ),
      p(
        className := "pt-4 text-xs tracking-widest uppercase text-clock-text",
        unit
      )
    )
  }

  // well, that's one way, with absolute and relative
  // but grid should be better, right?
  // private def renderFlipper(timePart: Signal[Int], unit: String) = {
  //   div(
  //     className := "flex flex-col justify-center",
  //     div(
  //       className := "relative w-10 h-10 bg-neutral-dark-blue",
  //       child.text <-- timePart.map(num => "%02d".format(num)),
  //       div(
  //         className := "absolute inset-x-0 bottom-0 h-1/2",
  //         className := "bg-neutral-desaturated-blue"
  //       )
  //     ),
  //     p(unit)
  //   )
  // }
}
