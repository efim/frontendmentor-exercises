package industries.sunshine.ipaddrtracker

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.ipaddrtracker.StateModel.AddressInfo

object InfoPanel {

  def render(state: Signal[AddressInfo]) = {
    ul(
      className := "my-5", // temporary, set in parent
      className := "flex flex-col gap-y-5 p-5 bg-white rounded-xl",
      className := "md:flex-row md:gap-x-10 md:justify-around md:items-start md:py-8 md:mt-10 md:w-4/5 md:h-36 md:divide-x-2 md:divide-solid",
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
      // this would be better done with grid layout in parent, but let's roll with flex
      className := "md:justify-items-start md:pl-7 md:w-64",
      // className := "md:items-center md:h-full",
      // className := "first:bg-yellow-200 first:rounded-t-xl", // not needed, parent has padding, owns the corners
      p(
        className := "font-bold uppercase tracking-[0.09rem] text-[0.5rem] text-dark-gray",
        className := "md:pb-3 md:text-xs",
        header
      ),
      p(
        className := "text-lg font-semibold",
        className := "md:text-2xl md:leading-none",
        child.text <-- value
      )
    )
  }
}
