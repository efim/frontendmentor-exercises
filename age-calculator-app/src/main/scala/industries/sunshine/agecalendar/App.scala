package industries.sunshine.agecalendar

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import scala.scalajs.js.Date

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
    div(
      className := "flex flex-col items-center bg-white rounded-xl w-[340px] h-[490px] rounded-ee-[3rem]",
      InputsComponent.renderInputs(pickedDate.writer),
      AgeDisplayComponent.renderAgeDisplay(pickedDate.signal)
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
