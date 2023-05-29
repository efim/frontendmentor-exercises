package industries.sunshine.interactivecommentsection

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import com.softwaremill.quicklens._
import industries.sunshine.interactivecommentsection.Models._
import java.time.Instant
import java.util.UUID

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
      className := "w-screen h-max bg-very-light-gray",
      mainTag(
        className := "p-4 pt-8 w-full h-full",
        CommentWallComponent.render(stateVar)
      ),
      renderAttribution()
    )
  }

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "absolute inset-x-0 top-0 attribution",
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
