package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._
import java.time.Instant

object MessageComponent {
  def render(
      messageSignal: Signal[Message],
      selfUser: AppUser,
      updateScore: Int => Unit,
      onReplySubmit: String => Unit
  ): Element = {
    val isReplyBoxEnabled = Var(false)
    lazy val replyBoxElement =
      MessageInputUI.render(selfUser, isReplyBoxEnabled.writer, onReplySubmit)
    lazy val emptyEl = emptyNode
    div(
      renderViewMode(messageSignal, isReplyBoxEnabled.writer, updateScore),
      child <-- isReplyBoxEnabled.signal.map(
        if (_) replyBoxElement else emptyEl
      )
    )
  }

  def renderViewMode(
      messageSignal: Signal[Message],
      shouldShowReply: Observer[Boolean],
      updateScore: Int => Unit
  ): Element = {
    div(
      className := "grid grid-cols-3 p-4 bg-white rounded-lg",
      div(
        className := "col-span-3",
        renderHeader(messageSignal)
      ),
      div(
        className := "col-span-3 col-start-1 py-4 text-light-gray break-words",
        child.text <-- messageSignal.map(_.content)
      ),
      div(
        className := "col-start-1 row-start-3",
        renderVotingControls(messageSignal, updateScore)
      ),
      div(
        className := "col-start-3 row-start-3",
        renderReplyButton(shouldShowReply)
      )
    )
  }

  private def renderReplyButton(shouldShowReply: Observer[Boolean]): Element = {
    button(
      className := "flex flex-row justify-end items-center pr-2 h-full font-semibold text-moderate-blue",
      img(
        src := "/images/icon-reply.svg",
        className := "pr-2"
      ),
      onClick --> Observer(_ => shouldShowReply.onNext(true)),
      "Reply"
    )

  }

  private def renderVotingControls(
      messageSignal: Signal[Message],
      updateScore: Int => Unit
  ): Element = {
    div(
      child <-- messageSignal.map(_.score).map( currentScore =>
        div(
          className := "flex flex-row justify-around items-center p-2 rounded-lg w-18 bg-very-light-gray",
          button(
            img(src := "/images/icon-plus.svg", className := "w-3 h-3"),
            onClick --> Observer { _ =>
              println("upvoting")
              // actual application would flatmap into Fetch.put to retister vote with backend
              // then either depend on the top level Var[AppState] to be synced via ws,
              // or maybe take the value of vote from the response and update to it
              // (there could have been other people voting)
              updateScore(currentScore + 1)
            }
          ),
          div(
            className := "font-semibold text-moderate-blue",
            child.text <-- messageSignal.map(_.score)
          ),
          button(
            img(src := "/images/icon-minus.svg", className := "w-3 h-1"),
            onClick --> Observer { _ =>
              println("downvoting")
              updateScore(currentScore - 1)
            }
          )
        )
      )
    )
  }

  private def renderHeader(messageSignal: Signal[Message]): Element = {
    div(
      // TODO for some reason space-x-8 didn't work at all
      className := "flex flex-row items-center",
      div(
        className := "pr-3",
        img(
          className := "w-8 h-8",
          src <-- messageSignal.map(_.user.image.png),
          alt := ""
        )
      ),
      div(
        className := "pr-3 font-semibold",
        child.text <-- messageSignal.map(_.user.username)
      ),
      // TODO use relative time library from github?
      div(
        className := "text-light-gray",
        child.text <-- messageSignal.map(_.createdAt.toString().take(10))
      )
    )
  }

}
