package industries.sunshine.todolist

import com.raquo.laminar.api.L.{*, given}

object Header {
  def render() = {
    headerTag(
      className := "grid items-center mt-9 mb-2 grid-cols-[1fr_30px]",
      className := "md:mt-16 md:mb-4",
      h1(
        className := "text-2xl font-bold text-white uppercase tracking-[0.35rem]",
        className := "md:text-4xl md:tracking-[0.75rem]",
        "todo"),
      img(
        src := "/images/icon-moon.svg",
        alt := "Switch to dark theme",
        className := "w-5 md:w-6",
      )
    )
  }
}
