package industries.sunshine.ipaddrtracker

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom
import upickle.default._

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.ipaddrtracker.BackgroundMap.Coords
import com.raquo.airstream.core.Observer
import industries.sunshine.ipaddrtracker.StateModel.AddressInfo

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    val state = initState()

    mainTag(
      className := "flex flex-col items-center w-screen h-screen",
      Apis.getSelfIp() --> Observer((initial: AddressInfo) => {
        println(s"got response $initial")
        state.writer.onNext(initial)
      }), // directly bind initial Stream to load self ip info
      BackgroundMap.render(
        state.signal.map(st => Coords(st.location.lat, st.location.lng))
      ),
      h1(
        "IP Address Tracker",
        className := "z-20 py-4 text-2xl font-semibold text-white"
      ),
      div(
        // className := "px-6",
        className := "z-20",
        Inputs.render(state.writer),
        InfoPanel.render(state.signal),
        renderAttribution()
      )
    )
  }

  def initState(): Var[StateModel.AddressInfo] = {
    val hardcoded = read[StateModel.AddressInfo](StateModel.sampleJson)

    val stateVar = Var(hardcoded)

    stateVar
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
