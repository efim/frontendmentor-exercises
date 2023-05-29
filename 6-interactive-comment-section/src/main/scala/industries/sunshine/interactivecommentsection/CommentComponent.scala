package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._
import java.util.UUID
import java.time.Instant

/** Component to manage whole Comment: with all it's replies
  */
object CommentComponent {
  def render(commentSignal: Signal[Comment], updateComment: (Comment => Comment) => Unit, currentUser: AppUser): Element = {
    def onReplySubmit(message: String): Unit = {

      val updateSelfWithReply: Comment => Comment = oldState => {
        val reply = Reply(
          Message(
            UUID.randomUUID().toString(),
            message,
            Instant.now(),
            0,
            currentUser
          ),
          oldState.message.user
        )

        oldState.modify(_.replies)(_.updated(reply.message.id, reply))
      }

      updateComment(updateSelfWithReply)
    }
    def onCommentScoreUpdate(newScore: Int): Unit = {
      println(s"noop comment score update $newScore")
    }
    def onReplyScoreUpdate(replyUid: String)(newScore: Int): Unit = {
      println(s"noop reply score update $newScore for $replyUid")
    }
    div(
      MessageComponent.render(
        commentSignal.map(_.message),
        currentUser,
        onCommentScoreUpdate,
        onReplySubmit
      ),
      renderReplies(commentSignal, currentUser, onReplyScoreUpdate, onReplySubmit)
    )
  }

  private def renderReplies(
      commentSignal: Signal[Comment],
      currentUser: AppUser,
      updateScore: String => Int => Unit,
      onReplySubmit: String => Unit
  ): Element = {
    div(
      className := "flex flex-row pt-3",
      div(
        className := "self-stretch pr-3 border-l-2 border-indigo-100 w-[2px]",
      ),
      div(
        className := "flex flex-col gap-y-3",
        children <-- commentSignal
          .map(_.replies.values.toList)
          .split(_.message.id)((key, initial, signal) => {
            MessageComponent.render(
              signal.map(_.message),
              currentUser,
              updateScore(key),
              onReplySubmit
            )
          })
      )
    )

  }

}
