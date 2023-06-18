package industries.sunshine.ipaddrtracker

import io.laminext.fetch.Fetch
import concurrent.ExecutionContext.Implicits.global
import upickle.default._
import industries.sunshine.ipaddrtracker.StateModel.AddressInfo
import com.raquo.airstream.core.EventStream

object Apis {

  val ipifyKey = "at_kxjQ1RWEj6dIsnWJwpOxHzuVlrPyT"

  def getSelfIp(): EventStream[AddressInfo] = {
    println("in getting self ip")
    // free api
    // Fetch
    //   .get(s"http://ip-api.com/json/")
    //   .text
    //   .map(resp => {
    //     println(s"got $resp")
    //     read[AddressInfo](resp.data)
    //   })

    // paid limited
    // Fetch
    //   .get(s"https://geo.ipify.org/api/v2/country,city?apiKey=${ipifyKey}")
    //   .text
    //   .map(resp => {
    //     println(s"got $resp")
    //     read[AddressInfo](resp.data)
    //   })

    val hardcoded = read[AddressInfo](StateModel.sampleJson)
    val modified = hardcoded.copy(location = hardcoded.location.copy(lat = 48.8647, lng = 2.349), ip = "3.3.3.3")
    EventStream.fromValue(modified)
  }

  def getIp(ip: String): EventStream[AddressInfo] = {
    println(s"in get ip for $ip")
    // free api
    // Fetch
    //   .get(
    //     s"https://api.seeip.org/geoip/$ip"
    //   )
    //   .text
    //   .map(resp => {
    //     read[AddressInfo](resp.data)
    //   })

    // paid limited
    // Fetch
    //   .get(
    //     s"https://geo.ipify.org/api/v2/country,city?apiKey=${ipifyKey}&ipAddress=${ip}"
    //   )
    //   .text
    //   .map(resp => {
    //     read[AddressInfo](resp.data)
    //   })

    val hardcoded = read[AddressInfo](StateModel.sampleJson)
    val modified = hardcoded.copy(location = hardcoded.location.copy(lat = 41.69411, lng = 44.83368), ip = "1.1.1.1")
    println(s"to return $modified")
    EventStream.fromValue(modified, false)
  }

}
