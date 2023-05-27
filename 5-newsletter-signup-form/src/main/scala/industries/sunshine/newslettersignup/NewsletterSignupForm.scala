package industries.sunshine.newslettersignup

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

@main
def ProductPreviewCardComponent(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    div(
      mainTag(
        className := "relative w-screen h-screen bg-white font-custom",
        renderNewsletterSignupComponent()
      ),
      renderAttribution()
    )
  }

  def renderNewsletterSignupComponent(): Element = {
    val isSuccessfullySubmittedVar = Var(false)
    val emailVar = Var[String]("")

    def collectSubmittedEmail(email: String): Unit = {
      println(s"> on submitting $email")
      emailVar.writer.onNext(email)
      isSuccessfullySubmittedVar.writer.onNext(true)
    }

    def resetState(): Unit = {
      println("resetting state")
      isSuccessfullySubmittedVar.writer.onNext(false)
      emailVar.writer.onNext("")
    }

    lazy val form = InputFormComponenent.rendedSignupForm(collectSubmittedEmail)
    lazy val successMessage = renderSuccessMessage(emailVar.signal, resetState)
    div(
      child <-- isSuccessfullySubmittedVar.signal.map(
        if (_) successMessage else form
      )
    )
  }

  def renderSuccessMessage(
      emailSignal: Signal[String],
      resetState: () => Unit
  ): Element = {
    def messageWithEmail(): Signal[String] = emailSignal.map(email =>
      s"A confirmation email has been sent to $email. Please open it and click the button inside to confirm your subscription."
    )
    div(
      className := "flex flex-col justify-between px-8 pt-36 h-screen text-lg",
      div(
        img(src := "/images/icon-success.svg", alt := "", aria.hidden := true),
        p(
          className := "pt-8 pb-6 text-4xl font-bold",
          "Thanks for subscribing!"
        ),
        p(
          className := "text-base",
          "A confirmation email has been sent to ",
          child <-- emailSignal.map(email => b(email)),
          ". Please open it and click the button inside to confirm your subscription."
        )
      ),
      div(
        className := "pb-14",
        button(
          className := "place-self-end w-full h-14 font-bold text-white bg-blue-900 rounded-lg",
          "Dismiss message"
        ),
        onClick --> Observer(_ => resetState())
      )
    )
  }

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "absolute inset-x-0 bottom-2 attribution",
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
