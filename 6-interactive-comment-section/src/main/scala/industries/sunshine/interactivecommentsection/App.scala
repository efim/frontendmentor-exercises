package industries.sunshine.interactivecommentsection

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

    val hardcoded = Models.hardcoded
    val stateVar = Var(hardcoded)

    div(
      className := "w-screen h-screen",
      mainTag(
        className := "p-4 pt-8 w-screen h-screen bg-very-light-gray",
        MessageComponent.prepareTopLevelCommentComponent(stateVar),
      ),
      renderAttribution()
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
