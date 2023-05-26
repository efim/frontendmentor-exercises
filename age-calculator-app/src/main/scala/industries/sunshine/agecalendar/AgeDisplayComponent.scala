package industries.sunshine.agecalendar

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import cats.syntax.all._
import com.raquo.laminar.api.L.{*, given}
import scala.scalajs.js.Date
import scala.annotation.meta.field

object AgeDisplayComponent {
  def renderAgeDisplay(birthdate: Signal[Option[Date]]): Element = {
    val ageOptSignal = birthdate.map(_.map(calculateAge(_)))
    def renderAgeLine(unit: String, age: Signal[Option[Int]]): Element = {
      val numbersStyle = "text-main-purple"
      div(
        className := "flex flex-row items-start items-end italic leading-[1.8rem] text-[1.8rem] font-fancy-sans bold",
        className := "tracking-tight leading-[3.6rem] lg:text-[3.3rem]",
        child <-- age.splitOption(
          (initial, signal) =>
            p(
              child.text <-- signal.map(_.toString),
              className := numbersStyle
            ),
          p("--", className := numbersStyle)
        ),
        p(
          unit,
          className := "px-1"
        )
      )
    }

    div(
      className := "px-3 w-full text-base italic font-thicker bold text-fancy-sans",
      className := "lg:px-0",
      renderAgeLine("years", ageOptSignal.map(_.map(_.years))),
      renderAgeLine("months", ageOptSignal.map(_.map(_.months))),
      renderAgeLine("days", ageOptSignal.map(_.map(_.days)))
    )
  }

  final case class Age(years: Int, months: Int, days: Int)

  /** @return
    *   (years, months, days)
    */
  private def calculateAge(birthdate: Date): Age = {
    val now = new Date()
    var years = now.getFullYear() - birthdate.getFullYear();
    var months = now.getMonth() - birthdate.getMonth();
    var days = now.getDate() - birthdate.getDate();

    // If the current month is before the birth month, or it's the same month but the day hasn't occurred yet,
    // then the person hasn't had their birthday this year, so subtract a year from the age.
    if (months < 0 || (months === 0 && days < 0)) {
      years -= 1;
    }

    // Adjust month calculation
    if (months < 0) {
      months += 12;
    }

    // Adjust day calculation
    if (days < 0) {
      val daysInLastMonth =
        new Date(now.getFullYear().toInt, now.getMonth().toInt, 0).getDate();
      days += daysInLastMonth
    }

    Age(years.toInt, months.toInt, days.toInt)
  }

}
