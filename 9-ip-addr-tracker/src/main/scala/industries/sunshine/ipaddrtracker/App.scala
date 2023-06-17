package industries.sunshine.ipaddrtracker

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
    // here will be app state
    mainTag(
      className := "flex flex-col items-center w-screen h-screen",
      BackgroundMap.render(),
      h1(
        "IP Address Tracker",
        className := "py-4 text-2xl font-semibold text-white"
      ),
      div(
        className := "px-6",
        renderInputs(),
        renderUI(),
        renderAttribution()
      )
    )
  }

  def renderInputs() = {
    val placeholderText = "Search for any IP address or domain"
    val placeholderTextSmall =
      "Search for IP address"
    val ipInput = Var("")
    form(
      className := "flex flex-row",
      input(
        className := "px-5 rounded-l-xl grow",
        placeholder <-- Utils.isMobileWidthStream.map(
          if (_) placeholderTextSmall else placeholderText
        ),
        typ := "text",
        required := true,
        pattern := """^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$""",
        title := "IPv4 address, like 192.168.0.1",
        controlled(
          value <-- ipInput,
          onInput.mapToValue --> ipInput
        )
      ),
      onMountInsert(ctx =>
        button(
          className := "grid place-content-center w-12 h-12 bg-black rounded-r-xl",
          typ := "submit",
          img(
            src := "/images/icon-arrow.svg",
            alt := "Start search"
          ),
          onClick.preventDefault.map(_ =>
            val validity = ctx.thisNode.ref.checkValidity()
            if (!validity) ctx.thisNode.ref.reportValidity()
            validity
          ) --> Observer(isFormValid => {
            println(s"submitted ${ipInput.now()} for form instate $isFormValid")
          })
        )
      )
    )
    // and this is another way to set validation message
    // <input type="text" value="" pattern="(\d|(\d,\d{0,2}))" oninvalid="this.setCustomValidity('ERROR_TEXT')" oninput="this.setCustomValidity('')"/>
    // or, via inContext { onInput --> checking field validity, setting customValidity on the ref }
  }

  def renderUI() = {
    div(
      """
  IP Address
  Location
  Timezone
    UTC <!-- add offset value dynamically using the API -->
  ISP

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
