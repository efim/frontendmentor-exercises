package industries.sunshine.launchcountdown

import com.raquo.laminar.api.L.{*, given}
import java.time.{Instant, LocalDateTime, Period}
import java.time.Duration
import java.time.temporal.TemporalUnit

object Clock {
  def render(countDownTo: LocalDateTime) = {
    val diffNowToTarget = Utils.diffNowToTarget(countDownTo)

    div(
      className := "px-8 w-full text-neutral-desaturated-blue",
      className := "grid grid-cols-4 justify-between",
      className := "md:inline-grid md:gap-x-5 md:w-auto md:grid-cols-[repeat(4,_150px)]",
      renderFlipper(diffNowToTarget.map(_.toDays().toInt), "Days"),
      renderFlipper(diffNowToTarget.map(_.toHoursPart().toInt), "Hours"),
      renderFlipper(diffNowToTarget.map(_.toMinutesPart().toInt), "Minutes"),
      renderFlipper(diffNowToTarget.map(_.toSecondsPart().toInt), "Seconds")
    )
  }

  private def renderFlipper(timePart: Signal[Int], unit: String) = {
    div(
      className := "grid justify-items-center grid-rows-[repeat(2,_60px)]",
      className := "md:grid-cols-1 md:grid-rows-[150px_auto]",
      // className := "grid grid-rows-2 items-center",
      div(
        className := "grid relative w-[60px] grid-rows-[repeat(2,_30px)]",
        // 140 x 145 and 20 in between
        className := "md:w-[140px] md:grid-rows-[repeat(2,_75px)]",
        div(
          className := "rounded-md md:rounded-xl bg-clock-top"
          // className := "h-1/2 bg-neutral-dark-blue"
        ),
        div(
          className := "rounded-md md:rounded-xl bg-clock-bottom"
          // className := "bg-neutral-desaturated-blue"
        ),
        div(
          className := "absolute row-span-2 row-start-1 place-self-center text-4xl text-primary-red",
          className := "md:text-8xl",
          child.text <-- timePart.map(num => "%02d".format(num))
        )
      ),
      p(
        className := "pt-4 uppercase md:z-20 tracking-[0.15rem] text-[0.5rem] text-clock-text",
        className := "md:text-xl md:tracking-widest",
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
