package industries.sunshine.news.pages.front

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

@main
def FrontPage(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element =
    div(
      className := "relative w-screen h-screen bg-green-200",
      page(),
      renderAttribution()
    )

  def page(): Element = {
    div(
      className := "font-inter",
      """
  Home
  New
  Popular
  Trending
  Categories

  The Bright Future of Web 3.0?

  We dive into the next evolution of the web that claims to put the power of the platforms back into the hands of the people.
  But is it really fulfilling its promise?

  Read more

  New

  Hydrogen VS Electric Cars
  Will hydrogen-fueled cars ever catch up to EVs?

  The Downsides of AI Artistry
  What are the possible adverse effects of on-demand AI image generation?

  Is VC Funding Drying Up?
  Private funding by VC firms is down 50% YOY. We take a look at what that means.

  01
  Reviving Retro PCs
  What happens when old PCs are given modern upgrades?

  02
  Top 10 Laptops of 2022
  Our best picks for various needs and budgets.

  03
  The Growth of Gaming
  How the pandemic has sparked fresh opportunities.

"""
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
