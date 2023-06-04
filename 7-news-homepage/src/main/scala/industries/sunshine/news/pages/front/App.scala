package industries.sunshine.news.pages.front

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.news.pages.front.Models.FrontPageState

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element =
    val appState = Var(Models.hardcoded)
    div(
      className := "relative w-screen h-screen bg-off-white",
      page(appState.signal),
      renderAttribution()
    )

  def page(appStateSignal: Signal[FrontPageState]): Element = {
    div(
      className := "p-4 font-inter",
      FeaturedStoryComponent.render(appStateSignal.map(_.headliner))
    )
  }

  def renderHeader() = div("""
  Home
  New
  Popular
  Trending
  Categories
""")

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