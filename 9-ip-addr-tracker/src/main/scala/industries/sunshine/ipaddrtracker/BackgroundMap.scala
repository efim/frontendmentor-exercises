package industries.sunshine.ipaddrtracker

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}

object BackgroundMap {

  def render() = {
    div(
      className := "-z-10 fixed h-full",
      topPicture(),
      map()
      )
  }

  private def topPicture() = {
    val resizeBus = EventBus[Unit]()
    dom.window.addEventListener("resize", _ => resizeBus.writer.onNext(()))
    def windowSize = dom.window.innerWidth.toInt
    val windowWidthStream =
      resizeBus.events.map(_ => windowSize).startWith(windowSize)
    val isMobileWidth = windowWidthStream.map(_ <= 375)

    div(
      img(
        src <-- isMobileWidth.map(if (_) "/images/pattern-bg-mobile.png"
                                  else "/images/pattern-bg-desktop.png"),
      )
    )
  }

  private def map() = {
    div(
      className := "bg-gray-300 h-full",
    )
  }
}
