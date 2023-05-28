package industries.sunshine.interactivecommentsection

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.interactivecommentsection.Models._
import com.softwaremill.quicklens._

object MessageComponent {
  def render(messageVar: Var[Message]): Element = {
    div(
      className := "grid grid-cols-3 p-4 bg-white rounded-lg",
      div(
        className := "col-span-3",
        renderHeader(messageVar.signal)
      ),
      div(
        className := "col-span-3 col-start-1 pt-4 text-light-gray",
        child.text <-- messageVar.signal.map(_.content)
      ),
      div(
        className := "row-start-3",
        "votes"
      ),
      div(
        className := "col-start-3 row-start-3",
        "Reply"
      )
    )
  }

  // private def renderVotingControls(messageVar: Var[Message]): Element = {
  //   div(
  //     className := "flex flex-row",

  //   )
  // }

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

  def prepareTopLevelCommentComponent(stateVar: Var[AppState]): Element = {
    div(
      onMountInsert(ctx => {
        val commentVar: Var[Models.Message] =
          stateVar.zoom(_.comments.head.message)((state, newMessage) => {
            state.modify(_.comments.at(0).message).setTo(newMessage)
          })(ctx.owner)
        MessageComponent.render(commentVar)
      })
    )
  }
}
