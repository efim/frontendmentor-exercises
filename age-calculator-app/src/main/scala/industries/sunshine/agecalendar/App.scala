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
          val potentialDate = new Date(s"$year-$month-$day")
          !potentialDate.getDate().isNaN()
      }
      // if either month or year are not set - must rely on separate check
      validIfDate.getOrElse(true)
    }
    def isDayValidByNumber(day: Int) = day >= 1 && day <= 31
    def isValidMonth(month: Int) = month >= 1 && month <= 12
    def isValidYear(year: Int) = year <= (new Date()).getFullYear()

    def setTheDate(): Unit = {
      (
        selectedYear.now(),
        selectedMonth.now(),
        selectedDate.now()
      ).tupled match {
        case Some((year, month, day)) =>
          pickedDate.writer.onNext(Some(new Date(s"$year-$month-$day")))
        case None => pickedDate.writer.onNext(None)
      }
    }

    def renderDatePartInput(
        name: String,
        state: Var[Option[Int]],
        validation: Validation[String, Seq[String], String]
    ) = {
      val inputUid = s"${UUID.randomUUID().toString()}_${name}_input"
      val inputElement = input(
        idAttr := inputUid,
        placeholder := "24",
        className := "w-10 text-[0.75rem]",
        typ := "number",
        minAttr := "1",
        maxAttr := "31"
      ).validated(validation)
      val component = div(
        className := "flex flex-col items-start w-10 appearance-none",
        label(name, forId := inputUid, className := "text-[0.5rem]"),
        inputElement
      )

      (component, inputElement)
    }

    // i guess there shold be first level of basic validation for day number
    // and second level when month and year are set, using the Some[Date]
    // still all inputs should have their own model

    val dayValidation: Validation[String, Seq[String], String] =
      V.nonBlank("Date should not be empty") &&
        V.custom("Date should be a number")(_.toIntOption.nonEmpty) &&
        V.custom("Must be a valid day")(dayStr =>
          isDayValidByNumber(dayStr.toInt)
        ) &&
        V.custom("Must be a valid date")(dayStr =>
          isDayValidForDate(dayStr.toInt)
        )

    val (dayInputComponent, dayInput) =
      renderDatePartInput("day", selectedDate, dayValidation)
    // val validatedMonthInput = renderDatePartInput("month", selectedMonth)
    // val validatedYearInput = renderDatePartInput("year", selectedYear)
    div(
      className := "flex flex-col items-center bg-white rounded-xl w-[340px] h-[490px] rounded-ee-[3rem]",
      div(
        className := "flex flex-row",
        dayInputComponent,
        child.maybe <-- dayInput.validationError.optionMap(errors =>
          span(
            cls := "text-red-700 text-sm",
            errors.map(error => div(error))
          )
        )
        // validatedMonthInput,
        // validatedYearInput
      ),
      div(
        className := "italic font-thicker bold text-fancy-sans text-main-purple",
        """
  -- years
  -- months
  -- days
"""
      )
    )
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
