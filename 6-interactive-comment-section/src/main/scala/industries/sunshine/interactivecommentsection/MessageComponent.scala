package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._
import java.time.Instant

object MessageComponent {
  def render(
      messageVar: Var[Message],
      selfUser: AppUser,
      onReplySubmit: String => Unit
  ): Element = {
    val isReplyBoxEnabled = Var(false)
    lazy val replyBoxElement =
      MessageInputUI.render(selfUser, isReplyBoxEnabled.writer, onReplySubmit)
    lazy val emptyEl = emptyNode
    div(
      renderViewMode(messageVar, isReplyBoxEnabled.writer),
      child <-- isReplyBoxEnabled.signal.map(
        if (_) replyBoxElement else emptyEl
      )
    )
  }

  def renderViewMode(messageVar: Var[Message], shouldShowReply: Observer[Boolean] ): Element = {
    div(
      className := "grid grid-cols-3 p-4 bg-white rounded-lg",
      div(
        className := "col-span-3",
        renderHeader(messageVar.signal)
      ),
      div(
        className := "col-span-3 col-start-1 py-4 text-light-gray",
        child.text <-- messageVar.signal.map(_.content)
      ),
      div(
        className := "col-start-1 row-start-3",
        renderVotingControls(messageVar)
      ),
      div(
        className := "col-start-3 row-start-3",
        renderReplyButton(shouldShowReply)
      )
    )
  }

  private def renderReplyButton(shouldShowReply: Observer[Boolean] ): Element = {
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

  private def renderVotingControls(messageVar: Var[Message]): Element = {
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
          messageVar.update(_.modify(_.score).f(_ + 1))
        }
      ),
      div(
        className := "font-semibold text-moderate-blue",
        child.text <-- messageVar.signal.map(_.score)
      ),
      button(
        img(src := "/images/icon-minus.svg", className := "w-3 h-1"),
        onClick --> Observer { _ =>
          println("downvoting")
          messageVar.update(_.modify(_.score).f(_ - 1))
        }
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

  def prepareCommentMessageComponent(
      stateVar: Var[Comment],
      currentUser: AppUser,
      onReplySubmit: String => Unit
  ): Element = {
    div(
      onMountInsert(ctx => {
        val messageVar: Var[Models.Message] =
          stateVar.zoom(_.message)((state, newMessage) => {
            state.modify(_.message).setTo(newMessage)
          })(ctx.owner)
        MessageComponent.render(
          messageVar,
          currentUser,
          onReplySubmit
        )
      })
    )
  }

  def prepareReplyMessageComponent(
      stateVar: Var[Comment],
      replyUid: String,
      currentUser: AppUser,
      onReplySubmit: String => Unit
  ): Element = {
    div(
      onMountInsert(ctx => {
        val messageVar: Var[Models.Message] =
          stateVar.zoom(_.replies.get(replyUid).getOrElse(Reply.empty).message)((state, newMessage) => {
            state.modify(_.replies.index(replyUid).message).setTo(newMessage)
          })(ctx.owner)
        MessageComponent.render(
          messageVar,
          currentUser,
          onReplySubmit
        )
      })
    )
  }

  def prepareTopLevelCommentComponent(
      stateVar: Var[AppState],
      onReplySubmit: String => Unit
  ): Element = {
    div(
      onMountInsert(ctx => {
        val commentVar: Var[Models.Message] =
          stateVar.zoom(_.comments.get("first-message").getOrElse(Models.Comment.empty).message)((state, newMessage) => {
            state.modify(_.comments.index("first-message").message).setTo(newMessage)
          })(ctx.owner)
        MessageComponent.render(
          commentVar,
          stateVar.now().currentUser,
          onReplySubmit
        )
      })
    )
  }
}
