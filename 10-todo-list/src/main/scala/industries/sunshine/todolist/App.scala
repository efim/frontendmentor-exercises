package industries.sunshine.todolist

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.todolist.StateModel.TaskDescription

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    val state = Var[List[TaskDescription]](List.empty)

    div(
      Background.render(),
      div(
        className := "grid gap-y-5 px-5",
        Header.render(),
        InputUI.render(newTask => state.update(_.prepended(newTask))),
        """

  <!-- Add dynamic number --> items left

  All
  Active
  Completed

  Clear Completed

  Drag and drop to reorder list
""",
      ),
      renderAttribution()
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
