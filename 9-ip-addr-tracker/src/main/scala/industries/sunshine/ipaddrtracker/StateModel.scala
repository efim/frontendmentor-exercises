package industries.sunshine.ipaddrtracker

import upickle.default._

object StateModel {

  final case class AddressInfo(
      ip: String,
      location: Location,
      domains: List[String],
      `as`: ProviderInfo,
      isp: String
  ) derives ReadWriter

  final case class Location(
      country: String,
      region: String,
      city: String,
      lat: Double,
      lng: Double,
      postalCode: String,
      timezone: String
  ) derives ReadWriter
  final case class ProviderInfo(
      asn: Int,
      name: String,
      route: String, // ip
      domain: String
  ) derives ReadWriter

  val sampleJson = """
{
    "ip": "8.8.8.8",
    "location": {
        "country": "US",
        "region": "California",
        "city": "Mountain View",
        "lat": 37.40599,
        "lng": -122.078514,
        "postalCode": "94043",
        "timezone": "-07:00",
        "geonameId": 5375481
    },
    "domains": [
        "0d2.net",
        "003725.com",
        "0f6.b0094c.cn",
        "007515.com",
        "0guhi.jocose.cn"
    ],
    "as": {
        "asn": 15169,
        "name": "Google LLC",
        "route": "8.8.8.0/24",
        "domain": "https://about.google/intl/en/",
        "type": "Content"
    },
    "isp": "Google LLC"
}
"""
}
