package industries.sunshine.agecalendar

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import cats.syntax.all._
import com.raquo.laminar.api.L.{*, given}
import scala.scalajs.js.Date
import java.util.UUID
import scala.annotation.meta.field
import io.laminext.syntax.core._
import io.laminext.syntax.validation.cats._
import io.laminext.syntax.validation._

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    div(
      className := "flex relative justify-center w-screen h-screen bg-off-white",
      mainTag(
        role := "main",
        div(
          className := "pt-[90px]",
          renderAgeCalendar()
        )
      ),
      renderAttribution()
    )
  }

  def renderAgeCalendar(): Element = {
    val pickedDate = Var[Option[Date]](None)
    val selectedDate = Var[Option[Int]](None)
    val selectedMonth = Var[Option[Int]](None)
    val selectedYear = Var[Option[Int]](None)

    def isDayValidForDate(day: Int): Boolean = {
      // if Month and Year are already set, check if Day results in possible Date
      val validIfDate = (selectedMonth.now(), selectedYear.now()).tupled.map {
        case (month, year) =>
          val potentialDate = new Date(year, month - 1, day)
          !potentialDate.getDate().isNaN()
      }
      // if either month or year are not set - must rely on separate check
      validIfDate.getOrElse(true)
    }
    def isDayValidByNumber(day: Int) = day >= 1 && day <= 31
    val dayValidation: Validation[String, Seq[String], String] =
      V.nonBlank("Date should not be empty") &&
        V.custom("Date should be a number")(_.toIntOption.nonEmpty) &&
        V.custom("Must be a valid day")(dayStr =>
          isDayValidByNumber(dayStr.toInt)
        ) &&
        V.custom("Must be a valid date")(dayStr =>
          isDayValidForDate(dayStr.toInt)
        )
    def isValidMonth(month: Int) = month >= 1 && month <= 12
    val monthValidation: Validation[String, Seq[String], String] =
      V.nonBlank("Month should not be empty") &&
        V.custom("Month should be a number")(_.toIntOption.nonEmpty) &&
        V.custom("Must be a valid month")(monthStr =>
          isValidMonth(monthStr.toInt)
        )

    /*
     * The JavaScript Date constructor interprets two-digit and one-digit year values as being in the 20th century.
     * so new Date(1, 0, 1) is 1st Jan 1901. ugh. valid date but age would jump around, let's allow only > 100
     * */
    def isValidYear(year: Int) = year <= (new Date()).getFullYear()
    val yearValidation: Validation[String, Seq[String], String] =
      V.nonBlank("Year should not be empty") &&
        V.custom("Year should be a number")(_.toIntOption.nonEmpty) &&
        V.custom("Year must be not smaller than 100")(yearStr =>
          yearStr.toInt >= 100
        ) &&
        V.custom("Year must be in the past")(yearStr =>
          yearStr.toInt <= (new Date()).getFullYear()
        )

    def setTheDate(): Unit = {
      val inputs = (
        selectedYear.now(),
        selectedMonth.now(),
        selectedDate.now()
      ).tupled
      println(s"inside of set the date with ${inputs}")

      inputs match {
        case Some((year, month, day)) =>
          val newDate = new Date(year, month - 1, day)
          println(s"setting new date: $newDate")
          pickedDate.writer.onNext(Some(new Date(year, month - 1, day)))
        case None => pickedDate.writer.onNext(None)
      }
    }

    def renderDatePartInput(
        name: String,
        state: Var[Option[Int]],
        validation: Validation[String, Seq[String], String],
        min: Option[Int] = None,
        max: Option[Int] = None
    ) = {
      val inputUid = s"${UUID.randomUUID().toString()}_${name}_input"
      val inputElement = input(
        idAttr := inputUid,
        placeholder := "24",
        className := "w-10 text-[0.75rem]",
        typ := "number",
        minAttr.maybe(min.map(_.toString())),
        maxAttr.maybe(max.map(_.toString())),
        // take input as String, after filter it's None for invalid, then set as value of Option[Int]
        onInput.mapToValue.map(strNum =>
          Some(strNum).filter(validation(_).isRight).flatMap(_.toIntOption)
        ) --> state.writer.onNext,
        // now this looks like a hack. "attempt to set full date with current states of all three fields"
        onInput.mapToValue --> Observer(_ => setTheDate())
      ).validated(validation)
      div(
        className := "flex flex-col items-start w-10 appearance-none",
        label(name, forId := inputUid, className := "text-[0.5rem]"),
        inputElement,
        child.maybe <-- inputElement.validationError.optionMap(errors =>
          span(
            cls := "text-red-700 text-[0.5rem]",
            errors.map(error => div(error))
          )
        )
      )
    }

    // i guess there shold be first level of basic validation for day number
    // and second level when month and year are set, using the Some[Date]
    // still all inputs should have their own model

    div(
      className := "flex flex-col items-center bg-white rounded-xl w-[340px] h-[490px] rounded-ee-[3rem]",
      div(
        className := "flex flex-row",
        renderDatePartInput("day", selectedDate, dayValidation, Some(1), Some(31)),
        renderDatePartInput("month", selectedMonth, monthValidation, Some(1), Some(12)),
        renderDatePartInput("year", selectedYear, yearValidation, Some(100), None)
      ),
      div(
        className := "italic font-thicker bold text-fancy-sans text-main-purple text-xs",
        child <-- pickedDate.signal.map(_.toString()),
        // child <-- pickedDate.signal.splitOption ( { case (initial, signal) =>
        //   div(
        //     child.text <-- signal.map(_.getUTCFullYear().toString())
        //   )
        // }, div("--") ),
        renderAgeDisplay(pickedDate.signal)
      )
    )
  }

  def renderAgeDisplay(birthdate: Signal[Option[Date]]): Element = {
    val ageOptSignal = birthdate.map(_.map(calculateAge(_)))
    def renderAgeLine(unit: String, age: Signal[Option[Int]]): Element = {
      div(
        child <-- age.splitOption( (initial, signal) =>
          p(
            child.text <-- signal.map(_.toString)
          )
          , p("--")),
        unit
      )
    }

    div(
      className := "italic font-thicker bold text-fancy-sans text-main-purple text-base",
      renderAgeLine("years", ageOptSignal.map(_.map(_.years))),
      renderAgeLine("months", ageOptSignal.map(_.map(_.months))),
      renderAgeLine("days", ageOptSignal.map(_.map(_.days))),
    )
  }

  final case class Age(years: Int, months: Int, days: Int)

  /** @return
    *   (years, months, days)
    */
  def calculateAge(birthdate: Date): Age = {
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
      val daysInLastMonth = new Date(now.getFullYear().toInt, now.getMonth().toInt, 0).getDate();
      days += daysInLastMonth
    }

    Age(years.toInt, months.toInt, days.toInt)
  }

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "absolute inset-x-0 bottom-2 attribution",
      "Challenge by ",
      a(
        href := "https://www.frontendmentor.io?ref=challenge",
        target := "_blank",
        "Frontend Mentor"
      ),
      " Coded by ",
      a(href := "#", "Your Name Here")
    )
  }

}
