package industries.sunshine.ipaddrtracker

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.ipaddrtracker.StateModel.AddressInfo

object InfoPanel {

  def render(state: Signal[AddressInfo]) = {
    ul(
      className := "my-5", // temporary, set in parent
      className := "flex flex-col gap-y-5 p-5 bg-white rounded-xl",
      renderItem("IP address", state.map(_.ip)),
      renderItem(
        "Location",
        state.map(st =>
          s"${st.location.country}, ${st.location.region}, ${st.location.city}"
        )
      ),
      renderItem("Timezone", state.map(st => s"UTC ${st.location.timezone}")),
      renderItem("ISP", state.map(_.isp))
    )
  }

  private def renderItem(header: String, value: Signal[String]) = {
    li(
      className := "grid justify-items-center bg-white grid-rows-[auto_1fr]",
      // className := "first:bg-yellow-200 first:rounded-t-xl", // not needed, parent has padding, owns the corners
      p(
        className := "font-bold uppercase tracking-[0.09rem] text-[0.5rem] text-dark-gray",
        header
      ),
      p(className := "text-lg font-semibold", child.text <-- value)
    )
  }
}
