package industries.sunshine.news.pages.front

import com.raquo.laminar.api.L.{ *, given }

object PageHeader {
  def render() = div("""
  Home
  New
  Popular
  Trending
  Categories
""")
}
