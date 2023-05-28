package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._

object  MessageInputUI {

  def render(selfUser: AppUser, shouldShowReply: Observer[Boolean], onSubmit: String => Unit): Element = {
    val replyText = Var("")
    div(
      className := "grid grid-cols-3 p-4 pb-0 mt-1 bg-white rounded-lg",
      onInput.mapToValue --> replyText,
      textArea(
        className := "col-span-3 row-start-1 p-3 pl-5 h-24 rounded-lg border text-light-gray",
        placeholder := "Add a comment...",
      ),
      div(
        className := "flex items-center h-20",
        img(
          src := selfUser.image.png,
          alt := "",
          className := "w-8 h-8",
        ),
      ),
      div(
        className := "flex col-start-3 justify-end items-center h-20",
        button(
          className := "w-24 h-12 font-semibold text-white rounded-lg bg-moderate-blue",
          onClick --> Observer { _ =>
            shouldShowReply.onNext(false)
            onSubmit(replyText.now())
            replyText.set("")
          },
          "SEND"
        )
      )
    )
  }
}
