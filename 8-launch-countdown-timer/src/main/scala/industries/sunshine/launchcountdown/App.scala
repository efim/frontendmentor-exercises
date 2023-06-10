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
      className := "w-screen h-screen",
      className := "text-2xl font-bold uppercase font-inter",
      renderBackground(),
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

  def renderBackground() = {
    div(
      className := "fixed w-full h-full bg-neutral-black-blue bg-pattern-stars",
      className := "-z-20",
      // className := "bg-gradient-to-b from-neutral-black-blue to-neutral-dark-blue",
      div(
        className := "fixed bottom-0 left-0 w-full h-1/3 bg-right bg-no-repeat bg-cover bg-pattern-hills"
      )
    )
  }

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "fixed inset-x-0 bottom-2 attribution text-neutral-white",
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
