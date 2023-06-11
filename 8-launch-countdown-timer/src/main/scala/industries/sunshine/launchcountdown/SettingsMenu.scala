package industries.sunshine.launchcountdown

import com.raquo.laminar.api.L.{*, given}
import java.util.UUID

object SettingsMenu {
  def render() = {
    val settingsDialog = renderModalMenu()

    div(
      button(
        img(
          src := "/images/grey-gear-svgrepo-com.svg",
          className := "w-10 ",
          alt := "menu"
        ),
        className := "fixed top-5 right-5 text-2xl text-primary-red",
        onClick --> Observer(_ => settingsDialog.ref.showModal()),
      ),
      settingsDialog,
    )
  }

  private def renderModalMenu() = {
    val formUid = UUID.randomUUID().toString()
    val targetInputId = s"${formUid}-target"
    val targetDateStr = Var("2023-01-07")

    dialogTag(
      form(
        method := "dialog",
        label("Countdown target:", forId := targetInputId),
        input(
          typ := "date",
          idAttr := targetInputId,
          controlled(
            value <-- targetDateStr,
            onInput.mapToValue --> targetDateStr,
          )
        ),
        button(
          typ := "submit",
          onClick --> Observer(value => {
                                 println(targetDateStr.now())
                               }),
          "save",
        ),
        button(
          typ := "submit",
          "cancel",
        ),

      )
    )
  }

}
