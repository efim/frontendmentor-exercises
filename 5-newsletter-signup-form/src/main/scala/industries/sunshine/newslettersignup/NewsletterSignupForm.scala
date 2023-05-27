package industries.sunshine.newslettersignup

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

@main
def NewsletterSignupPage(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    div(
      mainTag(
        className := "relative w-screen h-screen bg-white font-custom",
        className := "lg:bg-dark-bg",
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
    lazy val successMessage = SuccessMessageComponent.renderSuccessMessage(emailVar.signal, resetState)
    div(
      className := "lg:flex lg:flex-row lg:justify-center lg:items-center lg:h-full",
      child <-- isSuccessfullySubmittedVar.signal.map(
        if (_) successMessage else form
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
