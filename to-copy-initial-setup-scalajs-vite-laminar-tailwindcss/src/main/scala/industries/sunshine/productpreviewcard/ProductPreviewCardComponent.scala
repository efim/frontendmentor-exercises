package industries.sunshine.productpreviewcard

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

@main
def ProductPreviewCardComponent(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element =
    div(
      className := "bg-green-200 h-screen w-screen",
      a(
        href := "https://vitejs.dev",
        target := "_blank",
        img(src := "/vite.svg", className := "logo", alt := "Vite logo")
      ),
      a(
        href := "https://developer.mozilla.org/en-US/docs/Web/JavaScript",
        target := "_blank",
        "link to JS logo"
      ),
      h1("Hello Laminar!"),
      counterButton(),
      p(className := "read-the-docs", "Click on the Vite logo to learn more")
    )

  def counterButton(): Element = {
    val counter = Var(0)
    button(
      tpe := "button",
      "count is ",
      child.text <-- counter,
      onClick --> { event => counter.update(c => c + 1) }
    )
  }
}
