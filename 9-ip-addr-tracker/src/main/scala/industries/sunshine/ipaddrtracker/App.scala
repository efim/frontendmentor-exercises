package industries.sunshine.ipaddrtracker

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    // here will be app state
    // Var(state)
    mainTag(
      className := "flex flex-col items-center w-screen h-screen",
      BackgroundMap.render(),
      h1(
        "IP Address Tracker",
        className := "py-4 text-2xl font-semibold text-white"
      ),
      div(
        className := "px-6",
        Inputs.render(),
        InfoPanel.render(),
        renderAttribution()
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
