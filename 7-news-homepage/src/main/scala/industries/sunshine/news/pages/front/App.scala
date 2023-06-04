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
    val appState = Models.hardcoded
    div(
      className := "relative w-screen h-screen bg-off-white",
      page(appState),
      renderAttribution()
    )

  def page(appStateSignal: FrontPageState): Element = {
    div(
      className := "p-4 font-inter",
      className := "md:grid md:pt-40 md:grid-cols-[minmax(0,_20%)_1fr_minmax(0,_20%)]",
      renderContent(appStateSignal).amend(
        className := "col-start-2 row-start-2"
      )
    )
  }

  private def renderContent(appStateSignal: FrontPageState) = {
    div(
      div(
        className := "inline-grid gap-8",
        // could have been repeate(auto-fit but then sometimes > 3 columns
        // could have been good to get automatically moved new items on middle devices
        className := "md:grid-cols-[repeat(3,_minmax(var(--col-min-width),_1fr))]",
        FeaturedStoryComponent
          .render(appStateSignal.headliner)
          .amend(
            className := "md:col-span-2"
          ),
        NewStoriesComponent
          .render(appStateSignal.newArticles)
          .amend(className := "my-8 md:my-0"),
        SmallStoryCard.renderList(appStateSignal.recommended)
      )
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
