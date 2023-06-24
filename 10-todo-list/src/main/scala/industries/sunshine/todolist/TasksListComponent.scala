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
      children <-- tasks.split(_.uuid) {
        case (taskId, initial, taskSignal) => {
          renderSingleTask(
            taskSignal,
            setTaskCompletion(taskId),
            removeTask(taskId)
          )
        }
      }
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
        className := "relative mr-3 w-5 h-5",
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
          className := "absolute top-1/3 left-1/3 w-2",
          src := "/images/icon-check.svg",
          alt := "task is done",
        )
      ),
      p(
        className := "text-xs duration-200 grow",
        className <-- task.map(t =>
          if (t.isCompleted) "line-through text-light-grayish-blue" else ""
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
}
