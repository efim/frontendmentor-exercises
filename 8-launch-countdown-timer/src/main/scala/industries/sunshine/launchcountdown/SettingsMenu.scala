package industries.sunshine.launchcountdown

import com.raquo.laminar.api.L.{*, given}
import java.util.UUID
import java.time.Instant
import java.time.Duration

object SettingsMenu {
  def render(setNewDate: String => Unit) = {
    val settingsDialog = renderModalMenu(setNewDate)

    div(
      button(
        img(
          src := "/images/grey-gear-svgrepo-com.svg",
          className := "w-10",
          alt := "menu"
        ),
        className := "fixed top-5 right-5 text-2xl text-primary-red",
        onClick --> Observer(_ => settingsDialog.ref.showModal())
      ),
      settingsDialog
    )
  }

  private def renderModalMenu(setNewDate: String => Unit) = {
    val formUid = UUID.randomUUID().toString()
    val targetInputId = s"${formUid}-target"
    val targetDateStr = Var(Main.defaultDatetimeStr)

    val ticksUntilTarget = targetDateStr.signal.map(Utils.isoStringToInstant(_)).flatMap {
      case None => Signal.fromValue(Duration.ZERO)
      case Some(targetInstant) => Utils.diffNowToTarget(targetInstant)
    }.map(dur => {
            val days = dur.toDays()
            val hours = dur.toHoursPart()
            val mins = dur.toMinutesPart()
            val secs = dur.toSecondsPart()
            val milli = dur.toMillisPart()

            s"$days days; ${hours}:${mins}:${secs} - $milli"
          })

    dialogTag(
      onMountInsert(dialogCtx => {
        form(
          method := "dialog",
          label("Countdown target:", forId := targetInputId),
          input(
            typ := "datetime-local",
            idAttr := targetInputId,
            controlled(
              value <-- targetDateStr,
              onInput.mapToValue --> targetDateStr
            )
          ),
          button(
            typ := "submit",
            onClick --> Observer(value => {
              println(targetDateStr.now())
              setNewDate(targetDateStr.now())
            }),
            "save"
          ),
          button(
            "cancel",
            onClick.preventDefault --> Observer(_ =>
              dialogCtx.thisNode.ref.close()
            )
          ),
          p(
            "preview: ",
            child.text <-- ticksUntilTarget
          )
        )
      })
    )
  }

}
