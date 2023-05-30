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
      onReplySubmit: String => Unit,
      onDelete: () => Unit
  ): Element = {
    val isReplyBoxEnabled = Var(false)
    lazy val replyBoxElement =
      MessageInputUI.render(selfUser, isReplyBoxEnabled.writer, onReplySubmit, true)
    lazy val emptyEl = emptyNode
    div(
      renderViewMode(
        messageSignal,
        selfUser,
        isReplyBoxEnabled.writer,
        updateScore,
        onDelete
      ),
      child <-- isReplyBoxEnabled.signal.map(
        if (_) replyBoxElement else emptyEl
      )
    )
  }

  def renderViewMode(
      messageSignal: Signal[Message],
      selfUser: AppUser,
      shouldShowReplyWindow: Observer[Boolean],
      updateScore: Int => Unit,
      onDelete: () => Unit
  ): Element = {

    div(
      className := "grid grid-cols-3 p-4 bg-white rounded-lg",
      div(
        className := "col-span-3",
        renderHeader(messageSignal, selfUser)
      ),
      div(
        className := "col-span-3 col-start-1 py-4 break-words text-light-gray",
        child.text <-- messageSignal.map(_.content)
      ),
      div(
        className := "col-start-1 row-start-3",
        renderVotingControls(messageSignal, updateScore)
      ),
      div(
        className := "col-start-3 row-start-3 justify-end flex flex-row",
        child <-- messageSignal
          .map(_.user == selfUser)
          .map(
            if (_) renderOwnControls(onDelete)
            else renderReplyButton(shouldShowReplyWindow)
          )
      )
    )
  }

  private def renderOwnControls(
      onDelete: () => Unit
  ): Element = {
    val deletionDialog = dialogTag(
      className := "backdrop:bg-black/50 text-light-gray rounded-lg p-7",
      form(
        method := "dialog",
        p("Delete comment", className := "text-xl font-semibold pb-3"),
        p(
          className := "",
          "Are you sure you want to delete this comment? This will remove the comment and can't be undone."),
        div(
          className := "flex flex-row justify-between pt-3",
          button(
            className := "h-12 rounded-lg bg-light-gray text-white font-semibold w-full mr-3",
            `type` := "submit",
            onClick --> Observer(_ => println("cancelling")),
            "NO, CANCEL"
          ),
          button(
            className := "h-12 rounded-lg bg-soft-red text-white font-semibold w-full",
            `type` := "submit",
            onClick --> Observer(_ => onDelete()),
            onClick --> Observer(_ => println("submittign form")),
            "YES, DELETE"
          ),
        ),
      )
    )

    div(
      className := "flex flex-row items-center",
      deletionDialog,
      button(
        className := "flex flex-row font-bold text-soft-red items-center mr-7 text-sm",
        img(
          src := "/images/icon-delete.svg",
          alt := "",
          className := "h-4 mr-1"
        ),
        onClick --> Observer(_ => deletionDialog.ref.showModal()),
        "Delete"
      ),
      button(
        className := "flex flex-row font-bold text-moderate-blue items-center mr-5 text-sm",
        img(
          src := "/images/icon-edit.svg",
          alt := "",
          className := "h-4 mr-1"
        ),
        "Edit"
      )
    )
  }

  private def renderReplyButton(
      shouldShowReplyWindow: Observer[Boolean]
  ): Element = {
    button(
      className := "flex flex-row justify-end items-center pr-2 h-full font-semibold text-moderate-blue",
      img(
        src := "/images/icon-reply.svg",
        className := "pr-2"
      ),
      onClick --> Observer(_ => shouldShowReplyWindow.onNext(true)),
      "Reply"
    )

  }

  private def renderVotingControls(
      messageSignal: Signal[Message],
      updateScore: Int => Unit
  ): Element = {
    div(
      child <-- messageSignal
        .map(_.score)
        .map(currentScore =>
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

  private def renderHeader(
      messageSignal: Signal[Message],
      selfUser: AppUser
  ): Element = {
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
        className := "font-semibold",
        child.text <-- messageSignal.map(_.user.username)
      ),
      child <-- messageSignal
        .map(_.user == selfUser)
        .map(
          if (_)
            div(
              className := "h-5 text-xs font-semibold text-white rounded-sm bg-moderate-blue",
              className := "flex flex-row justify-center items-center px-1 ml-1",
              "you"
            )
          else emptyNode
        ),
      // TODO use relative time library from github?
      div(
        className := "pl-3 text-light-gray",
        child.text <-- messageSignal.map(_.createdAt.toString().take(10))
      )
    )
  }

}
