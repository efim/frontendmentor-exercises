package industries.sunshine.agecalendar

import com.raquo.laminar.api.L.{*, given}

import org.scalajs.dom

import cats.syntax.all._
import scala.scalajs.js.Date
import java.util.UUID
import io.laminext.syntax.core._
import io.laminext.syntax.validation.cats._

object InputsComponent {
  def renderInputs(birthDateVar: Var[Option[Date]]): Element = {
    val birthDateWriter = birthDateVar.writer
    val selectedDate = Var[Option[Int]](None)
    val selectedMonth = Var[Option[Int]](None)
    val selectedYear = Var[Option[Int]](None)
    def setTheDate(): Unit = {
      val inputs = (
        selectedYear.now(),
        selectedMonth.now(),
        selectedDate.now()
      ).tupled
      println(s"inside of set the date with ${inputs}")

      inputs match {
        case Some((year, month, day)) =>
          // set date to potentially invalid "1991-04-31" for whole form validation
          val newDate = new Date(s"${year}-${month}-${day}")
          println(s"setting new date: $newDate")
          birthDateWriter.onNext(Some(newDate))
        case None => birthDateWriter.onNext(None)
      }
    }

    def fullDateValidation(
        message: String
    ): Validation[String, Seq[String], String] = {
      V.custom(message)(_ =>
        {
          println(s"in full date validation with ${birthDateVar.now()}")
          birthDateVar
            .now()
            .fold(
              ifEmpty = true // not set yet
            )(date => !date.getDate().isNaN())
        }
      )
    }

    val yearValidation: Validation[String, Seq[String], String] = {
      V.custom("Must be in the past")(yearStr =>
        yearStr.toInt <= (new Date()).getFullYear()
      )
    }
    div(
      className := "flex flex-row justify-between px-3 pt-5 w-full",
      className := "lg:px-0 lg:space-x-4 lg:justify-normal",
      renderDatePartInput(
        "day",
        selectedDate,
        Some(fullDateValidation("Must be a valid date")),
        setTheDate,
        "DD",
        Some(1),
        Some(31)
      ),
      renderDatePartInput(
        "month",
        selectedMonth,
        Some(fullDateValidation("")),
        setTheDate,
        "MM",
        Some(1),
        Some(12)
      ),
      /*
       * The JavaScript Date constructor interprets two-digit and one-digit year values as being in the 20th century.
       * so new Date(1, 0, 1) is 1st Jan 1901. ugh. valid date but age would jump around, let's allow only > 100
       * */
      renderDatePartInput(
        "year",
        selectedYear,
        Some(yearValidation && fullDateValidation("")),
        setTheDate,
        "YYYY",
        Some(100),
        None
      )
    )
  }

  private def renderDatePartInput(
      name: String,
      state: Var[Option[Int]],
      customValidationOpt: Option[Validation[String, Seq[String], String]],
      setTheDate: () => Unit,
      placeholderNum: String,
      min: Option[Int] = None,
      max: Option[Int] = None
  ) = {
    val inputUid = s"${UUID.randomUUID().toString()}_${name}_input"

    val customValidation = customValidationOpt.getOrElse(V.pass)

    val commonValidation = V.nonBlank("Should not be empty") &&
      V.custom("Should be a number")(_.toIntOption.nonEmpty) &&
      V.custom(s"Must be not smaller than ${min.getOrElse(0)}")(yearStr =>
        min.fold(true)(value => yearStr.toInt >= value)
      ) &&
      V.custom(s"Must be not bigger than ${max.getOrElse(0)}")(yearStr =>
        max.fold(true)(value => yearStr.toInt <= value)
      )

    val inputElement = input(
      idAttr := inputUid,
      placeholder := placeholderNum.toString(),
      className := "p-1 rounded w-[85px] h-[50px] border-[1px]",
      className := "w-10 font-medium appearance-none text-[0.65rem] font-fancy-sans",
      className := "lg:p-2 lg:text-base lg:w-[160px] lg:h-[70px]",
      typ := "number",
      minAttr.maybe(min.map(_.toString())),
      maxAttr.maybe(max.map(_.toString())),
      // take input as String, after filter it's None for invalid, then set as value of Option[Int]
      onInput.mapToValue.map(strNum =>
        Some(strNum).filter(commonValidation(_).isRight).flatMap(_.toIntOption)
      ) --> state.writer.onNext,
      // now this looks like a hack. "attempt to set full date with current states of all three fields"
      onInput.mapToValue --> Observer(_ => setTheDate())
    ).validated(commonValidation && customValidation)

    div(
      className := "flex flex-col items-start w-10 lg:w-[160px]",
      label(
        name,
        forId := inputUid,
        className := "pb-1 font-medium tracking-widest uppercase text-[0.4rem] font-fancy-sans",
        className <-- inputElement.validationError.map {
          case Some(_) => "text-light-red"
          case None    => "text-smokey-grey"
        },
        className := "lg:text-[0.5rem]"
      ),
      // getting Element inside of ValidatedElement to access .amend and bind border color to validation
      inputElement.el.amend(
        className <-- inputElement.validationError.map {
          case Some(_) => "border-light-red"
          case None    => "border-smokey-grey"
        }
      ),
      child.maybe <-- inputElement.validationError.optionMap(errors =>
        span(
          cls := "text-light-red text-[0.45rem] font-fancy-sans italic font-thinner",
          errors.map(error => div(error))
        )
      )
    )
  }
}
