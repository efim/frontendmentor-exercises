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
      renderSeparator(),
      AgeDisplayComponent.renderAgeDisplay(pickedDate.signal)
    )
  }

  def renderSeparator(): Element = {
    div(
      className := "py-4 w-10/12",
      div(
        className := "relative z-0 w-full h-8",
        div(
          className := "absolute w-8 h-8 rounded-full transform -translate-x-1/2 bg-main-purple start-1/2"
        ),
        div(
          className := "absolute top-1/2 w-full transform -translate-y-1/2 bg-light-grey h-[2px] -z-10"
        ),
        div(
          img(src := "images/icon-arrow.svg", alt := ""),
          className := "absolute transform -translate-x-1/2 translate-y-1/2 start-1/2",
          className := "w-4 h-4"
        )
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
