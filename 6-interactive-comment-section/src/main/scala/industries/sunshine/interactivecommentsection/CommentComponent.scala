package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._
import java.util.UUID
import java.time.Instant

/**
 * Component to manage whole Comment:
 * with all it's replies
 */
object CommentComponent {
  def render(commentVar: Var[Comment], currentUser: AppUser): Element = {
    def onReplySubmit(message: String): Unit = {
      println(s"state before is ${commentVar.now()}")
      commentVar.update { comment =>
        val reply = Reply(
          Message(
            UUID.randomUUID().toString(),
            message,
            Instant.now(),
            0,
            currentUser
          ),
          comment.message.user
        )
        comment.modify(_.replies)(replies => replies.updated(reply.message.id, reply))
      }
      println(s"reply submit in COMMENT level $message")
      println(s"state now is ${commentVar.now()}")
    }
    div(
      "COMMENT, YAY",
      MessageComponent.prepareCommentMessageComponent(
        commentVar, currentUser, onReplySubmit
      ),

    )
  }

  def prepareCommentComponent(
      stateVar: Var[AppState],
      uid: String
  ): Element = {
    div(
      onMountInsert { ctx =>
          // well, let's use Map then.
        val commentVar: Var[Models.Comment] =
          stateVar.zoom(_.comments.get(uid).getOrElse(Models.Comment.empty))((state, newComment) => {
            state.modify(_.comments.index(uid)).setTo(newComment)
          })(ctx.owner)
        render(commentVar, stateVar.now().currentUser)
      }
    )
  }
}
