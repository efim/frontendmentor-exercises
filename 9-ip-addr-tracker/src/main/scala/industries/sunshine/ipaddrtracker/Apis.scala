package industries.sunshine.ipaddrtracker

import io.laminext.fetch.Fetch
import concurrent.ExecutionContext.Implicits.global
import upickle.default._
import industries.sunshine.ipaddrtracker.StateModel.AddressInfo
import com.raquo.airstream.core.EventStream

object Apis {
  val ipifyKey = ""
  def getSelfIp(): EventStream[AddressInfo] = {
    println("in getting self ip")
    // Fetch
    //   .get(s"https://geo.ipify.org/api/v2/country?apiKey=${ipifyKey}")
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
    // Fetch
    //   .get(
    //     s"https://geo.ipify.org/api/v2/country?apiKey=${ipifyKey}&ipAddress=${ip}"
    //   )
    //   .text
    //   .map(resp => {
    //     read[AddressInfo](resp.data)
    //   })

    val hardcoded = read[AddressInfo](StateModel.sampleJson)
    println("what")
    val modified = hardcoded.copy(location = hardcoded.location.copy(lat = 41.69411, lng = 44.83368), ip = "1.1.1.1")
    println(s"to return $modified")
    EventStream.fromValue(modified, false)
  }

}
