package example

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import Data.CategoryResult
import com.raquo.airstream.web.AjaxStream
import com.raquo.laminar.DomApi
import io.laminext.fetch._
import concurrent.ExecutionContext.Implicits.global

@main
def resultsSummaryComponent(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.resultsSummary(Data.hardcoded)
  )

object Main {
  def resultsSummary(results: List[CategoryResult]): Element = {
    val totalScore = results.map(_.score).sum / results.size
    div(
      mainTag(
        // TODO don't understand why h-screen results in part of header being higher than top on some narrow phones
        role := "main",
        className := "flex relative flex-col justify-center items-center w-screen h-full lg:h-screen",
        div(
          className := "flex flex-col items-center",
          className := "lg:flex-row lg:place-self-center rounded-[40px] lg:h-[500px] lg:w-[720px] lg:shadow-custom",
          renderTotal(totalScore),
          renderSummary(results)
        )
      ),
      renderAttribution()
    )
  }

  def renderTotal(totalScore: Int) = {
    div(
      className := "flex flex-col items-center w-full rounded-b-[30px]",
      className := "lg:justify-center lg:w-1/2 lg:h-full lg:rounded-[30px]",
      className := "bg-gradient-to-b from-[#7643FF] to-[#2E2CE9]",
      h1(
        "Your Result",
        className := "py-5 font-semibold text-[#D4CAFF]",
        className := "lg:py-0 lg:pb-6 lg:text-xl"
      ),
      div(
        className := "flex flex-col justify-center items-center py-10 w-32 h-32 bg-blue-900 rounded-full",
        className := "lg:py-12 lg:h-[200px] lg:w-[200px]",
        className := "bg-gradient-to-b from-[#4A23CC] to-[#4734f0]",
        p(
          s"$totalScore",
          className := "text-5xl font-extrabold text-white",
          className := "lg:text-6xl"
        ),
        p(
          "of 100",
          className := "block px-10 pt-2 text-sm font-bold text-[#8a80ff]",
          className := "lg:font-bold"
        )
      ),
      div(
        className := "flex flex-col items-center",
        h2(
          "Great",
          className := "pt-5 pb-2 text-xl font-bold text-white",
          className := "lg:p-4 lg:text-3xl"
        ),
        p(
          "You scored higher than 65% of the people who have taken these tests.",
          className := "px-14 pb-8 text-sm font-semibold text-center text-[#B4B6FF]",
          className := "lg:px-12 lg:pb-0 lg:text-base lg:font-normal lg:leading-5"
        )
      )
    )
  }

  def renderSummary(results: List[CategoryResult]) = {
    div(
      className := "flex flex-col px-7 w-full",
      className := "lg:relative lg:px-8 lg:w-1/2 lg:h-full lg:rounded-r-[40px]",
      h1(
        "Summary",
        className := "pt-4 pb-2 text-base font-bold lg:pt-8 lg:pb-4 lg:text-xl lg:font-extrabold lg:tracking-wide"
      ),
      results.map(renderCategoryScore(_)),
      div(
        className := "py-4 w-full h-full",
        className := "lg:pt-6",
        button(
          "Continue",
          className := "w-full h-12 text-base font-bold text-white rounded-full bg-[#303B59]",
          className := "duration-75 active:bg-[#4535F0]"
        )
      )
    )
  }
  def renderCategoryScore(result: CategoryResult): Element = {

    val svgRaw = Fetch.get(result.iconPath).text.map(_.data)
    def loadingDiv = div(className := "lds-dual-ring")
    val svgIcon = svgRaw
      .map(str =>
        foreignSvgElement(DomApi.unsafeParseSvgString(str))
          .amend(svg.className := "justify-self-center w-4 h-4 lg:w-5 lg:h-5")
      )
      .startWith(loadingDiv)

    div(
      className := "py-2 w-full", // TODO for some reason no white space between categories without this
      div(
        className := "grid grid-cols-7 items-center px-2 w-full h-12 rounded-lg", // TODO for some reason justify-items-center doesn't work
        styleAttr := s"--custom-bg: ${result.bgColor}; --custom-highlight: ${result.highlightColor}",
        className := "bg-[--custom-bg]",
        className := "lg:h-12 lg:rounded-lg",
        child <-- svgIcon,
        p(
          className := "col-span-4 text-sm font-bold lg:text-base text-[--custom-highlight]",
          result.name
        ),
        div(
          className := "flex flex-row col-span-2 justify-self-center text-sm lg:text-base",
          p(s"${result.score}", className := "pr-1 font-semibold"),
          p(" / 100", className := "font-semibold text-gray-400")
        )
      )
    )
  }

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "absolute inset-x-0 bottom-0 attribution",
      "Challenge by ",
      a(
        href := "https://www.frontendmentor.io?ref=challenge",
        target := "_blank",
        "Frontend Mentor"
      ),
      " Coded by ",
      a(href := "#", "Your Name Here")
    )
  }
}
