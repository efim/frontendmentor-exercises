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
      className := "relative pb-4 w-full h-full bg-off-white",
      page(appState),
      renderAttribution()
    )

  def page(appStateSignal: FrontPageState): Element = {
    div(
      className := "font-inter",
      // this doesn't prefer decreasing empty space as much as i want.
      // className := "md:grid md:pt-40 md:grid-cols-[minmax(0,_20%)_1fr_minmax(0,_20%)]",
      // let's try to get left and right empty space with flex-box
      className := "flex flex-col items-center",
      // className := "relative", // for the mobile menu "overlay"
      div(
        className := "p-4 space-y-4 max-w-[1150px] width-full",
        PageHeader.render(),
        renderContent(appStateSignal),
      )
    )
  }

  private def renderContent(appStateSignal: FrontPageState) = {
    div(
        className := "grid gap-8 pb-12",
        // could have been repeate(auto-fit but then sometimes > 3 columns
        // could have been good to get automatically moved new items on middle devices
        className := "md:grid-cols-content",
        FeaturedStoryComponent
          .render(appStateSignal.headliner)
          .amend(
            className := "md:col-span-2"
          ),
        NewStoriesComponent
          .render(appStateSignal.newArticles)
          .amend(className := "my-8 md:my-0"),
        SmallStoryCard
          .renderList(appStateSignal.recommended).map(card =>
          div(
            // NOTE: wow, this is the reason for new stories not to get transferred to row 2
            // why would that be?
            // className := "md:row-start-2",
            // right now just by flex & max-width kind of assuring that not small cards are getting into top row
            card
          ))
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
