package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._
import java.util.UUID
import java.time.Instant

/** Component to manage whole Comment: with all it's replies
  */
object CommentComponent {
  def render(
      commentSignal: Signal[Comment],
      updateComment: (Comment => Comment) => Unit,
      deleteComment: () => Unit,
      currentUser: AppUser
  ): Element = {

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
      val changeCommentScore: Comment => Comment = comment => {
        comment.modify(_.message.score).setTo(newScore)
      }
      updateComment(changeCommentScore)
    }
    def onCommentMessageTextUpdate(newText: String): Unit = {
      val changeCommentScore: Comment => Comment = comment => {
        comment.modify(_.message.content).setTo(newText)
      }
      updateComment(changeCommentScore)
    }

    def onReplyScoreUpdate(replyId: String)(newScore: Int): Unit = {
      val changeReployScore: Comment => Comment = comment => {
        comment.modify(_.replies.index(replyId).message.score).setTo(newScore)
      }
      updateComment(changeReployScore)
    }
    def onReplyMessageTextUpdate(replyId: String)(newText: String): Unit = {
      val changeReployScore: Comment => Comment = comment => {
        comment.modify(_.replies.index(replyId).message.content).setTo(newText)
      }
      updateComment(changeReployScore)
    }

    def onReplyDelete(replyId: String): () => Unit = () => {
      val withReplyDeleted: Comment => Comment = comment => {
        comment.modify(_.replies)(_.removed(replyId))
      }
      updateComment(withReplyDeleted)
    }

    div(
      MessageComponent.render(
        commentSignal.map(_.message),
        currentUser,
        onCommentScoreUpdate,
        onCommentMessageTextUpdate,
        onReplySubmit,
        deleteComment
      ),
      child <-- commentSignal
        .map(_.replies.isEmpty)
        .map(
          if (_) emptyNode
          else
            renderReplies(
              commentSignal,
              currentUser,
              onReplyScoreUpdate,
              onReplyMessageTextUpdate,
              onReplySubmit,
              onReplyDelete
            )
        )
    )
  }

  private def renderReplies(
      commentSignal: Signal[Comment],
      currentUser: AppUser,
      updateScore: String => Int => Unit,
      updateMessageText: String => String => Unit,
      onReplySubmit: String => Unit,
      onReplyDelete: String => () => Unit,
  ): Element = {
    div(
      className := "flex flex-row pt-3",
      className := "lg:pt-5",
      div(
        // would be better done by div "vertical line" in another div
        // then on mobile could do items-start, on desktop items-center, but let's hack
        className := "self-stretch pr-3 border-l-2 border-indigo-100 w-[2px]",
        className := "lg:ml-11 lg:w-11",
      ),
      div(
        className := "flex flex-col gap-y-3 w-full",
        className := "lg:gap-y-5",
        children <-- commentSignal
          .map(
            _.replies.values.toList.sortBy(_.message.createdAt.toEpochMilli())
          )
          .split(_.message.id)((replyUid, initial, signal) => {
            MessageComponent.render(
              signal.map(_.message),
              currentUser,
              updateScore(replyUid),
              updateMessageText(replyUid),
              onReplySubmit,
              onReplyDelete(replyUid),
            )
          })
      )
    )

  }

}
