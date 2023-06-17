package industries.sunshine.ipaddrtracker

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}

object Utils {
  val isMobileWidthStream = {
    val resizeBus = EventBus[Unit]()
    dom.window.addEventListener("resize", _ => resizeBus.writer.onNext(()))
    def windowSize = dom.window.innerWidth.toInt
    val windowWidthStream =
      resizeBus.events.map(_ => windowSize).startWith(windowSize)
    val isMobileWidth = windowWidthStream.map(_ <= 375)
    isMobileWidth
  }

}
