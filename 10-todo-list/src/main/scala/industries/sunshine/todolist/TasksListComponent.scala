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
      input(
        typ := "checkbox",
        checked <-- task.map(_.isCompleted),
        className := "appearance-none cursor-pointer",
        className := "mr-3 w-5 h-5 rounded-full border border-light-grayish-blue",
        className := "checked:bg-yellow-300",
        onInput.mapToChecked --> Observer(setTaskCompletion(_))
      ),
      p(
        className := "grow",
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
