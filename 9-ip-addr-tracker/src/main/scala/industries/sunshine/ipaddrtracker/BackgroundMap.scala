package industries.sunshine.ipaddrtracker

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}

object BackgroundMap {

  final case class Coords(
    lat: Double,
    lgn: Double
  )

  def render(coods: Signal[Coords]) = {
    div(
      className := "fixed h-full -z-10",
      topPicture(),
      map()
    )
  }

  private def topPicture() = {
    div(
      img(
        src <-- Utils.isMobileWidthStream.map(
          if (_) "/images/pattern-bg-mobile.png"
          else "/images/pattern-bg-desktop.png"
        )
      )
    )
  }

  private def map() = {
    div(
      className := "h-full bg-gray-300"
    )
  }
}
