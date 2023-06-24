package industries.sunshine.todolist

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.todolist.StateModel.TaskDescription

object TasksListComponent {
  def render(
      tasks: Signal[List[TaskDescription]],
      setTaskCompletion: String => Boolean => Unit,
      removeTask: String => () => Unit
  ) = {
    div(
      div(
        className := "flex flex-col divide-y divide-very-light-grayish-blue",
        children <-- tasks.split(_.uuid) {
          case (taskId, initial, taskSignal) => {
            renderSingleTask(
              taskSignal,
              setTaskCompletion(taskId),
              removeTask(taskId)
            )
          }
        },
        renderListFooter()
      ),
      renderBottomInfo()
    )

  }

  private def renderSingleTask(
      task: Signal[TaskDescription],
      setTaskCompletion: Boolean => Unit,
      deleteTask: () => Unit
  ) = {
    div(
      className := "flex flex-row items-center p-3 px-4 bg-white",
      className := "first:rounded-t",
      div(
        className := "relative mr-2 w-5 h-5",
        input(
          typ := "checkbox",
          checked <-- task.map(_.isCompleted),
          className := "absolute appearance-none cursor-pointer",
          className := "w-5 h-5 rounded-full border border-light-grayish-blue",
          className := "checked:bg-gradient-to-br checked:to-primary-check-purple checked:from-primary-check-blue",
          className := "overflow-hidden",
          onInput.mapToChecked --> Observer(setTaskCompletion(_))
        ),
        img(
          className := "absolute top-1/3 left-1/3 w-2 pointer-events-none",
          src := "/images/icon-check.svg",
          alt := "task is done"
        )
      ),
      p(
        className := "text-xs duration-200 grow",
        className <-- task.map(t =>
          if (t.isCompleted) "line-through text-light-grayish-blue"
          else "text-very-dark-grayish-blue"
        ),
        child.text <-- task.map(_.description)
      ),
      button(
        img(
          src := "/images/icon-cross.svg",
          alt := "delete the task",
          className := "h-3"
        ),
        onClick --> Observer(_ => deleteTask())
      )
    )
  }

  /** counter, controls to clear all, and apply filters on mobile - in two bars,
    * on desktop - as single bar doing with very confusing grid layout and
    * manual corner rounding, so that white backround would be on elements, and
    * not on Gap
    */
  private def renderListFooter() = {
    def renderCount() = {
      val a = 1
      p(
        className := "p-3 px-5 text-xs bg-white rounded-bl text-dark-grayish-blue",
        "items left"
      )
    }

    def renderClearCompleted() = {
      val a = 1
      p(
        className := "p-3 px-5 text-xs bg-white rounded-br text-dark-grayish-blue",
        "Clear Completed"
      )
    }

    def renderFilters() = {
      val a = 1
      div(
        className := "grid",
        className := "text-sm font-bold bg-white",
        div(
          className := "inline-grid place-content-center grid-cols-[repeat(3,_auto)]",
          className := "text-xs text-dark-grayish-blue",
          button(className := "px-2", "All"),
          button(className := "px-2", "Active"),
          button(className := "px-2", "Completed")
        )
      )

    }

    div(
      className := "grid gap-y-4 grid-cols-[auto_1fr_auto]",
      className := "text-sm",
      renderCount(),
      div(className := "bg-white"), // empty space for mobile view
      renderClearCompleted(),
      renderFilters().amend(
        className := "col-span-full",
        className := "p-3 rounded"
      )
    )
  }

  def renderBottomInfo() = p(
    className := "mt-10 text-sm text-center text-very-dark-grayish-blue",
    "Drag and drop to reorder list"
  )

}
