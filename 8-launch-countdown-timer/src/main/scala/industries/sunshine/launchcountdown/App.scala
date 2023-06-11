package industries.sunshine.launchcountdown

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    val countDownToInLocal = initState()

    def setNewDate(dateString: String): Unit = {
      val localDateTime = Utils.isoStringToInstant(dateString)

      println(s"setting new $localDateTime")
      localDateTime.foreach(countDownToInLocal.writer.onNext(_))
    }

    div(
      className := "w-screen h-screen",
      className := "font-inter",
      // className := "text-2xl font-bold uppercase font-inter",
      renderBackground(),
      renderContent(countDownToInLocal.signal),
      renderAttribution(),
      SettingsMenu
        .render(setNewDate)
        .amend(
          className := "md:visible collapse"
        )
    )
  }

  def renderContent(targetDate: Signal[LocalDateTime]) = {
    div(
      className := "flex flex-col items-center h-full",
      div(
        className := "md:h-1/6 h-[130px]"
      ),
      h1(
        className := "w-4/5 text-2xl text-center",
        className := "uppercase tracking-[0.3rem] text-neutral-white md:tracking-[0.6rem]",
        "We're launching soon "
      ),
      child <-- targetDate.map(targetDate =>
        Clock
          .render(targetDate)
          .amend(
            className := "pt-16 md:pt-32"
          ),
      ),
      div(
        className := "grow"
      ),
      div(
        className := "w-36 h-24",
        div(
          className := "flex justify-between",
          a(
            href := "",
            img(
              src := "/images/icon-facebook.svg"
            )
          ),
          a(
            href := "",
            img(
              src := "/images/icon-pinterest.svg"
            )
          ),
          a(
            href := "",
            img(
              src := "/images/icon-instagram.svg"
            )
          )
        )
      )
    )
  }

  def renderBackground() = {
    div(
      className := "fixed w-full h-full bg-neutral-black-blue bg-pattern-stars",
      className := "-z-20",
      // className := "bg-gradient-to-b from-neutral-black-blue to-neutral-dark-blue",
      div(
        className := "fixed bottom-0 left-0 w-full h-1/3 bg-right-top bg-no-repeat bg-cover bg-pattern-hills",
        className := "md:h-1/4"
      )
    )
  }

  val defaultDatetimeStr = {
    val dateTime = LocalDateTime.now().plusDays(14)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val result = dateTime.format(formatter)
    println(s"the initial date is $result")
    result
  }

  def initState(): Var[LocalDateTime] = {
    val countDownTo = Utils
      .isoStringToInstant(defaultDatetimeStr)
      .get
      .atZone(ZoneId.systemDefault())
      .toInstant()

    val browserTZ = js.Dynamic.global.Intl
      .DateTimeFormat()
      .resolvedOptions()
      .timeZone
      .asInstanceOf[String]
    // println(s"> $zoneId ; $countDownToInLocal")

    val zoneId = ZoneId.of(browserTZ)
    val countDownToInLocal = LocalDateTime.ofInstant(countDownTo, zoneId)

    Var(countDownToInLocal)
  }

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "fixed inset-x-0 bottom-2 attribution text-neutral-white",
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
