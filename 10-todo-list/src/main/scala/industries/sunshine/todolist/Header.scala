package industries.sunshine.todolist

import com.raquo.laminar.api.L.{*, given}

object Header {
  def render() = {
    headerTag(
      className := "grid items-center mt-9 mb-7 grid-cols-[1fr_30px]",
      h1(
        className := "text-2xl font-bold text-white uppercase tracking-[0.35rem]",
        "todo"),
      img(
        src := "/images/icon-moon.svg",
        alt := "Switch to dark theme",
        className := "w-5",
      )
    )
  }
}
