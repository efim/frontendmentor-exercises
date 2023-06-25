package industries.sunshine.todolist

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import upickle.default._

object Theme {
  val darkThemeLocalStorage = "dark-theme-data"
  private val isDarkTheme = getState()
  val isDarkThemeSignal = isDarkTheme.signal
  def toggleDarkTheme(): Unit = {
    isDarkTheme.update(!_)
    saveState()
  }
  def renderToggler() = {
    button(
      img(
        src <-- isDarkThemeSignal.map(
          if (_) "/images/icon-sun.svg" else "/images/icon-moon.svg"
        ),
        alt := "Toggle dark theme",
        className := "w-5 md:w-6"
      ),
      onClick --> Observer(_ => toggleDarkTheme())
    )
  }

  private def getState() = {
    val raw = Option(dom.window.localStorage.getItem(darkThemeLocalStorage))
    val isDarkTheme = raw.map(read[Boolean](_))
    Var(isDarkTheme.getOrElse(false))
  }
  private def saveState() = {
    println(s"writing dark theme ${isDarkTheme.now()}")
    dom.window.localStorage
      .setItem(darkThemeLocalStorage, write[Boolean](isDarkTheme.now()))
  }
}
