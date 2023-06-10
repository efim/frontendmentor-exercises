package industries.sunshine.launchcountdown

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
  def appElement(): Element =
    div(
      className := "w-screen h-screen bg-neutral-dark-blue",
      className := "font-bold text-5xl uppercase font-inter",
      div(
        className := "text-neutral-white",
        """
  We're launching soon

  Days
  Hours
  Minutes
  Seconds
"""
      ),
      renderAttribution()
    )

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "fixed inset-x-0 bottom-2 attribution",
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
