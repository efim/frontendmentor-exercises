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
    div(
      className := "w-screen h-screen flex flex-col items-center",
      BackgroundMap.render(),
      h1(
        "IP Address Tracker",
        className := "text-2xl text-white font-semibold py-4"
      ),
      renderInputs(),
      renderUI(),
      renderAttribution()
    )
  }

  def renderInputs() = {
    val placeholderText = "Search for any IP address or domain"
    val ipInput = Var("")
    form(
      input(
        placeholder := placeholderText,
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
            // if (!isFormValid) println("hello")
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
