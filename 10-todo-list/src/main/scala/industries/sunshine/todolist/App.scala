package industries.sunshine.todolist

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import upickle.default._
import industries.sunshine.todolist.StateModel.TaskDescription

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    val state = getState()
    def onTaskSubmit(task: TaskDescription): Unit = {
      state.update(_.prepended(task))
      saveState(state.now())
    }

    div(
      Background.render(),
      div(
        className := "grid gap-y-5 px-5",
        Header.render(),
        InputUI.render(onTaskSubmit(_)),
        TasksListComponent.render(state.signal),
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

  val stateStorageKey = "stateStorageKey"
  def getState() = {
    val raw = Option(dom.window.localStorage.getItem(stateStorageKey))
    val saved = raw.map(read[List[TaskDescription]](_))
    println(s"got unpickled state: $saved")
    val state = saved.getOrElse(List.empty)
    Var[List[TaskDescription]](state)
  }
  def saveState(state: List[TaskDescription]): Unit = {
    val pickled = write(state)
    dom.window.localStorage.setItem(stateStorageKey, pickled)
    println(s"should save $pickled")
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