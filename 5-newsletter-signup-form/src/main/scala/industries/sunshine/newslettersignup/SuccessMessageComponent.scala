package industries.sunshine.newslettersignup

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

object SuccessMessageComponent {
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
          className := "place-self-end w-full h-14 font-bold text-white rounded-lg bg-grey-dark-slate",
          className := "duration-100 hover:bg-gradient-to-r hover:from-button-left hover:to-button-right",
          "Dismiss message"
        ),
        onClick --> Observer(_ => resetState())
      )
    )
  }

}
