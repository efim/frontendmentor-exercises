package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._
import java.time.Instant
import com.raquo.laminar.tags.HtmlTag
import com.raquo.laminar.codecs.StringAsIsCodec

import scala.scalajs.js
import scala.scalajs.js.annotation.*
@js.native
@JSImport("@github/relative-time-element", JSImport.Namespace)
object RelativeTimeElement extends js.Object

object MessageComponent {
  def render(
      messageSignal: Signal[Message],
      selfUser: AppUser,
      updateScore: Int => Unit,
      updateMessageText: String => Unit,
      onReplySubmit: String => Unit,
      onDelete: () => Unit
  ): Element = {
    val isReplyBoxEnabled = Var(false)
    val isEditModeEnabled = Var(false)
    lazy val replyBoxElement =
      MessageInputUI.render(
        selfUser,
        isReplyBoxEnabled.writer,
        onReplySubmit,
        true
      )
    lazy val emptyEl = emptyNode
    def enableEditMode(): Unit = {
      println("enabling edit")
      isEditModeEnabled.writer.onNext(true)
    }
    lazy val viewMode = renderViewMode(
      messageSignal,
      selfUser,
      isReplyBoxEnabled.writer,
      updateScore,
      onDelete,
      enableEditMode
    )
    lazy val editMode = renderEditMode(
      messageSignal,
      selfUser,
      updateMessageText,
      onDelete,
      () => isEditModeEnabled.writer.onNext(false)
    )

    div(
      child <-- isEditModeEnabled.signal.map(if (_) editMode else viewMode),
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
      onDelete: () => Unit,
      enableEditMode: () => Unit
  ): Element = {

    div(
      className := "grid grid-cols-3 p-4 bg-white rounded-lg",
      className := "lg:pt-4 lg:pt-6 lg:grid-cols-[75px_1fr_1fr]",
      div( // HEADER
        className := "col-span-3",
        className := "lg:col-start-2 lg:row-start-1",
        renderHeader(messageSignal, selfUser)
      ),
      div( // TEXT
        className := "col-span-3 col-start-1 py-4 break-words text-light-gray",
        className := "lg:col-span-2 lg:col-start-2 lg:row-start-2",
        child.text <-- messageSignal.map(_.content)
      ),
      div( // VOTE CONTROL
        className := "col-start-1 row-start-3",
        className := "lg:col-start-1 lg:row-span-3 lg:row-start-1 lg:p-2",
        renderVotingControls(messageSignal, updateScore)
      ),
      div( // REPLY \ OWN CONTROL
        className := "flex flex-row col-start-3 row-start-3 justify-end",
        className := "lg:col-start-3 lg:row-start-1",
        child <-- messageSignal
          .map(_.user == selfUser)
          .map(
            if (_) renderOwnControls(onDelete, enableEditMode)
            else renderReplyButton(shouldShowReplyWindow)
          )
      )
    )
  }

  // this is with technical debt, not passing whole selfUser would be better
  def renderEditMode(
      messageSignal: Signal[Message],
      selfUser: AppUser,
      onMessageSubmit: String => Unit,
      onDelete: () => Unit,
      disableEditMode: () => Unit
  ): Element = {

    val textInput = textArea(
      className := "col-span-3 col-start-1 py-3 px-4 my-4 break-words text-light-gray",
      className := "rounded-lg border outline-none focus:border-moderate-blue",
      value <-- messageSignal.map(_.content),
      onMountFocus
    )

    div(
      className := "grid grid-cols-3 p-4 bg-white rounded-lg",
      div(
        className := "col-span-3",
        renderHeader(messageSignal, selfUser)
      ),
      textInput,
      div(
        className := "flex flex-row col-start-3 row-start-3 justify-end",
        renderOwnControls(onDelete, () => textInput.ref.focus())
      ),
      button(
        className := "w-24 h-10 font-semibold text-white rounded-lg bg-moderate-blue",
        className := "hover:bg-light-grayish-blue",
        onClick --> Observer(_ => {
          onMessageSubmit(textInput.ref.value)
          disableEditMode()
        }),
        "UPDATE"
      )
    )
  }

  private def renderOwnControls(
      onDelete: () => Unit,
      onEdit: () => Unit
  ): Element = {
    val deletionDialog = dialogTag(
      className := "p-7 rounded-lg backdrop:bg-black/50 text-light-gray",
      className := "lg:w-[400px] lg:h-[260px] ",
      form(
        method := "dialog",
        p("Delete comment", className := "pb-3 text-xl font-semibold lg:text-2xl "),
        p(
          className := "lg:mb-4",
          "Are you sure you want to delete this comment? This will remove the comment and can't be undone."
        ),
        div(
          className := "flex flex-row justify-between pt-3",
          button(
            className := "mr-3 w-full h-12 font-semibold text-white rounded-lg bg-light-gray",
            `type` := "submit",
            onClick --> Observer(_ => println("cancelling")),
            "NO, CANCEL"
          ),
          button(
            className := "w-full h-12 font-semibold text-white rounded-lg bg-soft-red",
            `type` := "submit",
            onClick --> Observer(_ => onDelete()),
            onClick --> Observer(_ => println("submittign form")),
            "YES, DELETE"
          )
        )
      )
    )

    div(
      className := "flex flex-row items-center",
      deletionDialog,
      button(
        className := "flex flex-row items-center mr-7 text-sm font-bold text-soft-red",
        className := "hover:text-pale-red",
        img(
          src := "/images/icon-delete.svg",
          alt := "",
          className := "mr-1 h-4"
        ),
        onClick --> Observer(_ => deletionDialog.ref.showModal()),
        "Delete"
      ),
      button(
        className := "flex flex-row items-center mr-5 text-sm font-bold text-moderate-blue",
        className := "hover:text-light-grayish-blue",
        img(
          src := "/images/icon-edit.svg",
          alt := "",
          className := "mr-1 h-4"
        ),
        onClick --> Observer(_ => onEdit()),
        "Edit"
      )
    )
  }

  private def renderReplyButton(
      shouldShowReplyWindow: Observer[Boolean]
  ): Element = {
    button(
      className := "flex flex-row justify-end items-center pr-2 h-full font-semibold text-moderate-blue",
      className := "hover:text-light-grayish-blue",
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
            className := "lg:flex-col lg:w-10 lg:h-24",
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


  val relativeTimeTag = htmlTag("relative-time")
  val relTimeDatetime = htmlAttr("datetime", StringAsIsCodec)
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
        child <-- messageSignal.map(message => {
          val scalaInstant = message.createdAt
          val isoTime =
            java.time.Instant.ofEpochSecond(scalaInstant.getEpochSecond())
          relativeTimeTag(
            scalaInstant.toString().take(10),
            relTimeDatetime := isoTime.toString()
          )
        })
      )
    )
  }

}
