package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._

import java.time.Instant
import java.util.UUID

object CommentWallComponent {

  /** Component that displays all of the comments in the state and new comment
    * ui
    */
  def render(stateVar: Var[AppState]): Element = {
    div(
      className := "lg:w-[730px]",
      child <-- stateVar.signal
        .map(_.currentUser)
        .map(renderComments(_, stateVar)),
      renderTopLevelCommentSubmission(stateVar)
    )
  }

  /** Single div of all Comments visible from the perspective of currentUser
    */
  def renderComments(currentUser: AppUser, stateVar: Var[AppState]): Element = {
    // TODO - Comments (top level) and replies will have
    // different on submit, potentially different URLs
    def onReplySubmit(commentUid: String)(message: String): Unit = {
      stateVar.update { state =>
        val comment = state.comments.get(commentUid).get
        val reply = Reply(
          Message(
            UUID.randomUUID().toString(),
            message,
            Instant.now(),
            0,
            state.currentUser
          ),
          comment.message.user
        )
        state.modify(_.comments.at(commentUid).replies)(replies =>
          replies.updated(reply.message.id, reply)
        )
      }
      println(s"reply submit in WALL level $message")
    }

    def updateComment(commentUid: String)(f: Comment => Comment): Unit = {
      stateVar.update(_.modify(_.comments.index(commentUid))(f))
    }
    def deleteComment(commentUid: String): () => Unit = () => {
      stateVar.update(_.modify(_.comments)(_.removed(commentUid)))
    }

    div(
      className := "flex flex-col space-y-3 lg:space-y-5",
      children <-- stateVar.signal
        .map(_.comments.values.toList.sortBy(_.message.score)(Ordering.Int.reverse))
        .split(_.message.id)((commentId, initial, signal) => {
          div(
            CommentComponent.render(
              signal,
              updateComment(commentId),
              deleteComment(commentId),
              stateVar.now().currentUser // this can be done better, but ok
            )
          )
        })
    )
  }

  def renderTopLevelCommentSubmission(stateVar: Var[AppState]): Element = {
    def onSubmit(currentUser: AppUser)(message: String): Unit = {
      val newComment = Comment(
        Message(
          UUID.randomUUID().toString(),
          message,
          Instant.now(),
          0,
          currentUser
        ),
        Map.empty
      )
      stateVar.update(
        _.modify(_.comments)(_.updated(newComment.message.id, newComment))
      )
    }
    div(
      className := "pt-2 lg:pt-5",
      child <-- stateVar.signal
        .map(_.currentUser)
        .map(curUser => MessageInputUI.render(curUser, Observer.empty, onSubmit(curUser), false))
    )
  }
}
