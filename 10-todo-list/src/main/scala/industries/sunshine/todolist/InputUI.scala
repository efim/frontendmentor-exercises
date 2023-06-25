package industries.sunshine.todolist

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.todolist.StateModel.TaskDescription

object InputUI {
  def render(onSubmitTask: TaskDescription => Unit) = {
    val inputState = Var("")
    div(
      className := "flex flex-row items-center p-3 px-4 bg-white rounded",
      className := "dark:bg-dt-very-dark-desaturated-blue",
      className := "md:p-4 md:px-5",
      div(
        className := "mr-3 w-5 h-5 rounded-full border border-light-grayish-blue",
        className := "md:mr-4",
        className := "dark:border-dt-very-dark-grayish-blue-2"
      ),
      form(
        input(
          className := "text-xs md:text-base md:tracking-tighter grow",
          className := "border-none appearance-none outline-none",
          className := "placeholder:text-dt-very-dark-grayish-blue dark:bg-dt-very-dark-desaturated-blue dark:text-dt-light-grayish-blue",
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
          inputState.set("")
        )
      )
    )
  }
}
