package industries.sunshine.newslettersignup

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import io.laminext.syntax.validation._
import java.util.UUID

object InputFormComponenent {
  def rendedSignupForm(collectSubmittedEmail: String => Unit): Element = {

    val screenResizeEventsBus = EventBus[Unit]()
    dom.window.addEventListener(
      "resize",
      _ => screenResizeEventsBus.writer.onNext(())
    )
    val windowWidthStream = screenResizeEventsBus.events
      .map(_ => dom.window.innerWidth.toInt)
      .startWith(dom.window.innerWidth.toInt)
    val isMobileWidthStream: Signal[Boolean] =
      windowWidthStream.map(_ < 1024).distinct

    val dynamicImage = img(
      src <-- isMobileWidthStream.map(
        if (_) "/images/illustration-sign-up-mobile.svg"
        else
          "/images/illustration-sign-up-desktop.svg"
      ),
      alt := "",
      aria.hidden := true
    )

    div(
      dynamicImage,
      div(
        className := "p-6",
        h1(className := "pt-5 pb-5 text-4xl font-bold", "Stay updated!"),
        p("Join 60,000+ product managers receiving monthly updates on:"),
        ul(
          className := "py-4",
          renderListItem("Product discovery and building what matters"),
          renderListItem("Measuring to ensure updates are a success"),
          renderListItem("And much more!")
        ),
        renderInputForm(collectSubmittedEmail)
      )
    )
  }

  def renderInputForm(collectSubmittedEmail: String => Unit) = {
    val emailFieldContent = Var("")
    val formUid = UUID.randomUUID().toString()

    val inputItem = input(
      className := "px-4 w-full rounded-lg border h-[3.3rem]",
      className := "border-grey",
      className := "focus:outline-none focus:border-grey-charcoal",
      typ := "email",
      forId := formUid,
      placeholder := "email@company.com",
      onInput.mapToValue --> emailFieldContent
    ).validated(V.email("Valid email required"))

    form(
      className := "pt-8",
      div(
        className := "flex justify-between pb-2 text-xs",
        label(className := "font-bold", "Email address"),
        child <-- inputItem.validationError.splitOption(
          { (initialErrs, signal) =>
            p(
              className := "text-tomato",
              child.text <-- signal.map(_.mkString("\n"))
            )
          },
          emptyNode
        )
      ),
      inputItem.el.amend(
        className <-- inputItem.validationError.splitOption(
          { (iniital, signal) =>
            "bg-input-error text-tomato"
          },
          "bg-white text-black"
        )
      ),
      div(
        className := "pt-6",
        input(
          className := "w-full font-bold text-white rounded-lg bg-grey-dark-slate h-[3.3rem]",
          className := "duration-100 hover:bg-gradient-to-r hover:from-button-left hover:to-button-right",
          typ := "submit",
          value := "Subscribe to monthly newsletter",
          idAttr := formUid
        )
      ),
      onSubmit.preventDefault --> Observer(_ => {
        val email = emailFieldContent.now()
        collectSubmittedEmail(email)
      })
    )
  }

  def renderListItem(text: String) = {
    li(
      className := "flex flex-row items-start py-1",
      div(
        img(
          src := "/images/icon-list.svg",
          className := "transform translate-y-1/4",
          alt := "",
          aria.hidden := true
        )
      ),
      div(className := "pl-4", text)
    )
  }

}
