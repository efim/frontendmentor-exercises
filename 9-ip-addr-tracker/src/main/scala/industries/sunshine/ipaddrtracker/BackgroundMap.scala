package industries.sunshine.ipaddrtracker

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import typings.leaflet.global.L.tileLayer
import typings.leaflet.mod.TileLayerOptions
import typings.leaflet.mod.LatLngLiteral
import typings.leaflet.mod.Map_
import typings.leaflet.mod.Marker_
import typings.leaflet.global.L.DivIcon_
import typings.leaflet.mod.MarkerOptions
import typings.leaflet.mod.Icon_
import typings.leaflet.mod.IconOptions
import typings.leaflet.mod.MapOptions
import typings.leaflet.mod.ZoomPanOptions

object BackgroundMap {

  final case class Coords(
      lat: Double,
      lng: Double
  ) {
    def toLeaflet() = {
      LatLngLiteral(lat, lng)
    }
  }

  def render(coords: Signal[Coords]) = {
    div(
      className := "flex fixed flex-col h-full",
      topPicture(),
      map(coords)
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

  private def map(coods: Signal[Coords]) = {
    var mapVar: Option[Map_] = None
    var markerVar: Option[Marker_[Any]] = None
    div(
      idAttr := "map",
      className := "w-screen bg-gray-300 grow", // i don't know why max doens't work, let it be screen
      onMountCallback(ctx => {
        val defalutCoords = LatLngLiteral(51.505, -0.09)
        val mapInstance = typings.leaflet.mod
          .map("map", MapOptions().setZoomControl(false))
          .setView(defalutCoords, 13)
        val layer = tileLayer(
          "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
          TileLayerOptions()
            .setMaxZoom(19)
            .setAttribution(
              """&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>"""
            )
        ).addTo(mapInstance)
        val marker =
          typings.leaflet.mod
            .marker(
              defalutCoords,
              MarkerOptions().setIcon(
                Icon_(IconOptions(iconUrl = "/images/icon-location.svg"))
              )
            )
            .addTo(mapInstance)
        markerVar = Some(marker)

        mapVar = Some(mapInstance)
      }),
      coods.map(_.toLeaflet()) --> Observer((newCoords: LatLngLiteral) =>
        markerVar.foreach(_.setLatLng(newCoords))
        mapVar.foreach(_.flyTo(newCoords, 13))
        // mapVar.foreach(_.setView(newCoords, (), ZoomPanOptions().setAnimate(true).setDuration(2).setEaseLinearity(2)))
      )
    )
  }
}
