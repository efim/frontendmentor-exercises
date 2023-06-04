package industries.sunshine.news.pages.front

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object PageHeader {

  def render() = {
    val resizeEventBus = EventBus[Unit]()
    dom.window.addEventListener("resize", _ => resizeEventBus.writer.onNext(()))
    val screenSizesStream =
      resizeEventBus.events.map(_ => dom.window.innerWidth.toInt)
    val isMobileSignal = screenSizesStream
      .map(_ < 1024)
      .distinct
      .startWith(dom.window.innerWidth < 1024)

    lazy val mobileMenu = renderMobileControl()
    lazy val desktopMenu = renderDesktopControls()

    div(
      className := "flex flex-row items-center h-14",
      div(
        className := "flex-1",
        img(
          src := "/images/logo.svg",
          alt := "Company logo - letter 'W'",
          className := "w-12",
        ),
      ),
      div(
        child <-- isMobileSignal.map( if (_) mobileMenu else desktopMenu)
      )

    )
  }

  private def renderMobileControl() = button(
    img(
      src := "/images/icon-menu.svg",
      alt := "navigation menu"
    )
  )

  private def renderDesktopControls() = div("""
  Home
  New
  Popular
  Trending
  Categories
""")
}
