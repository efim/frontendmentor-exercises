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
  val localStorageState = "app-state"

  def appElement(): Element = {

    val stateVar = initState()
    div(
      className := "w-screen h-max bg-very-light-gray",
      mainTag(
        className := "p-4 pt-8 w-full h-full",
        className := "lg:flex lg:flex-row lg:justify-center lg:pt-[80px] lg:pb-[80px]",
        CommentWallComponent.render(stateVar)
      ),
      renderAttribution(),
      stateVar.signal --> Observer(writeState(_)),
    )
  }

  private def initState(): Var[AppState] = {
    val hardcoded = Models.hardcoded

    val savedStateString = Option(
      dom.window.localStorage.getItem(localStorageState)
    ).filterNot(_.isEmpty())
    val savedStateOpt = savedStateString.flatMap(stateStr =>
      scala.util.Try(upickle.default.read[AppState](stateStr)).toOption
    )
    val stateVar = Var(savedStateOpt.getOrElse(hardcoded))

    stateVar
  }

  private def writeState(updated: AppState): Unit = {
    dom.window.localStorage
      .setItem(localStorageState, upickle.default.write(updated))
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
