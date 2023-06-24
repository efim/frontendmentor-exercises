package industries.sunshine.todolist

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.todolist.StateModel.TaskDescription

object InputUI {
  def render(onSubmitTask: TaskDescription => Unit) = {
    val inputState = Var("")
    div(
      className := "flex flex-row items-center p-3 px-4 bg-white rounded",
      div(
        className := "mr-3 w-5 h-5 rounded-full border border-light-grayish-blue"
      ),
      form(
        input(
          className := "text-xs grow",
          className := "border-none appearance-none outline-none",
          typ := "text",
          placeholder := "Create a new todo...",
          required := true,
          controlled(
            value <-- inputState,
            onInput.mapToValue --> inputState
          )
        ),
        onSubmit.preventDefault --> Observer(_ =>
          println(s"submitting form with ${inputState.now()}")
          onSubmitTask(TaskDescription(inputState.now(), false))
        )
      )
    )
  }
}
