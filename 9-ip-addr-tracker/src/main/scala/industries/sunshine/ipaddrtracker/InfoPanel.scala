package industries.sunshine.ipaddrtracker

import com.raquo.laminar.api.L.{*, given}

object InfoPanel {

  def render() = {
    ul(
      className := "my-5", // temporary, set in parent
      className := "flex flex-col gap-y-5 p-5 bg-white rounded-xl",
      renderItem("IP address", "123.214.123.123"),
      renderItem("Location", "Tbilisi"),
      renderItem("Timezone", "UTC +4"),
      renderItem("ISP", "Magti")
    )
  }

  private def renderItem(header: String, value: String) = {
    li(
      className := "grid justify-items-center bg-white grid-rows-[auto_1fr]",
      // className := "first:bg-yellow-200 first:rounded-t-xl", // not needed, parent has padding, owns the corners
      p(className := "font-bold uppercase tracking-[0.09rem] text-[0.5rem] text-dark-gray", header),
      p(className := "text-lg font-semibold", value)
    )
  }
}
