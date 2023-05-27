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

    lazy val form = InputFormComponenent.rendedSignupForm(collectSubmittedEmail)
    lazy val successMessage = renderSuccessMessage()
    div(
      child <-- isSuccessfullySubmittedVar.signal.map(
        if (_) successMessage else form
      )
    )
  }

  def renderSuccessMessage(): Element = {
    div(
      className := "text-lg",
      """
  Thanks for subscribing!

  A confirmation email has been sent to ash@loremcompany.com.
  Please open it and click the button inside to confirm your subscription.

  Dismiss message
"""
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
