package industries.sunshine.ipaddrtracker

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.ipaddrtracker.StateModel.AddressInfo

object Inputs {
  def render(stateWriter: Observer[AddressInfo]) = {
    val placeholderText = "Search for any IP address or domain"
    val placeholderTextSmall =
      "Search for IP address"
    val ipInput = Var("")
    form(
      className := "flex flex-row md:h-14 md:w-[550px]",
      input(
        className := "px-5 rounded-l-xl grow",
        placeholder <-- Utils.isMobileWidthStream.map(
          if (_) placeholderTextSmall else placeholderText
        ),
        typ := "text",
        required := true,
        pattern := """^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$""",
        title := "IPv4 address, like 192.168.0.1",
        controlled(
          value <-- ipInput,
          onInput.mapToValue --> ipInput
        )
      ),
      onMountInsert(ctx =>
        button(
          className := "grid place-content-center w-12 bg-black rounded-r-xl md:w-14",
          typ := "submit",
          img(
            src := "/images/icon-arrow.svg",
            alt := "Start search"
          ),
          onClick.preventDefault
            .map(_ =>
              val validity = ctx.thisNode.ref.checkValidity()
              if (!validity) ctx.thisNode.ref.reportValidity()
              validity
            )
            .flatMap(_ => Apis.getIp(ipInput.now())) --> Observer(
            (result: AddressInfo) => {
              println("in the press")
              stateWriter.onNext(result)
              // Apis
              //   .getIp(ipInput.now())
              //   .map(newData =>
              //     println(s"calling on next... ${newData.location}")
              //     stateWriter.onNext(newData))
            }
          )
        )
      )
    )
    // and this is another way to set validation message
    // <input type="text" value="" pattern="(\d|(\d,\d{0,2}))" oninvalid="this.setCustomValidity('ERROR_TEXT')" oninput="this.setCustomValidity('')"/>
    // or, via inContext { onInput --> checking field validity, setting customValidity on the ref }
  }

}
