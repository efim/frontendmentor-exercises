package industries.sunshine.interactivecommentsection

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import com.softwaremill.quicklens._
import industries.sunshine.interactivecommentsection.Models.Reply
import industries.sunshine.interactivecommentsection.Models.Message
import java.time.Instant
import java.util.UUID

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {

    val hardcoded = Models.hardcoded
    val stateVar = Var(hardcoded)
    // TODO - Comments (top level) and replies will have
    // different on submit, potentially different URLs
    def onReplySubmit(message: String): Unit = {
      println(s"state before is ${stateVar.now().comments.head}")
      stateVar.update { state =>
        val comment = state.comments.get("first-message").get
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
        state.modify(_.comments.at("first-message").replies)(replies => replies.updated(reply.message.id, reply))
      }
      println(s"reply submit in TOP level $message")
      println(s"state now is ${stateVar.now().comments.head}")
    }

    div(
      className := "w-screen h-screen",
      mainTag(
        className := "p-4 pt-8 w-screen h-screen bg-very-light-gray",
        CommentComponent.prepareCommentComponent(stateVar, "second-message")
      ),
      renderAttribution()
    )
  }

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "absolute inset-x-0 bottom-2 attribution",
      "Challenge by ",
      a(
        href := "https://www.frontendmentor.io?ref=challenge",
        target := "_blank",
        "Frontend Mentor"
      ),
      " Coded by ",
      a(href := "#", "Your Name Here")
    )
  }
}
