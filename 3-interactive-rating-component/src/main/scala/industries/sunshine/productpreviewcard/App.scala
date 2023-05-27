package industries.sunshine.productpreviewcard

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import com.raquo.airstream.state.Var

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    div(
      mainTag(
        role := "main",
        className := "flex relative flex-col w-screen h-screen bg-[#121417]",
        div(
          className := "flex flex-grow justify-center items-center",
          renderVotingComponent()
        )
      ),
      renderAttribution()
    )
  }

  /* full voting component to be usable after adding to dom
   * starts state shared by ui parts and logic for switching between them
   */
  def renderVotingComponent() = {
    val selectedVote = Var[Option[Int]](None)
    var isSubmitted = Var[Boolean](false)

    // for simpler components it's good enough to pass Var into subcomponent
    // for more complex might be good to pass in limited Signal \ update function
    // that way it could be easier to reason about possible interactions
    def markSubmitted(unit: Unit): Unit = {
      // in application with backend we could set another boolean variable in case submission was successful
      selectedVote.signal.now() match {
        case None    => () // do nothing, no value selected
        case Some(_) => isSubmitted.writer.onNext(true)
      }
    }
    def isSelectedVoteSignal(forVoteButton: Int) =
      selectedVote.signal.map {
        case Some(`forVoteButton`) => true
        case _                     => false
      }
    def setVote(vote: Int) = {
      selectedVote.writer.onNext(Some(vote))
      println(s"voting $vote. current vote is ${selectedVote.signal.now()}")
    }

    div(
      child <-- isSubmitted.signal.map(
        if (_)
          renderThankYouUI(selectedVote.signal.now())
        else
          renderRatingSelectionUI(setVote, isSelectedVoteSignal, markSubmitted)
      )
    )
  }

  def renderRatingSelectionUI(
      setVote: Int => Unit,
      isSelectedVoteSignal: Int => Signal[Boolean],
      markSubmitted: Unit => Unit
  ): Element = {
    val votes = List(1, 2, 3, 4, 5)

    div(
      className := "bg-gradient-to-b rounded-2xl from-blue-dark to-blue-very-dark h-[350px] w-[325px]",
      className := "lg:h-[415px] lg:w-[412px] lg:rounded-[2rem]",
      className := "p-7 lg:p-9 lg:pr-12",
      div(
        className := "flex justify-center items-center w-10 h-10 rounded-full bg-gray-dark",
        className := "lg:w-14 lg:h-14",
        img(src := "/images/icon-star.svg", aria.hidden := true, alt := "")
      ),
      h1(
        className := "py-4 text-2xl font-bold text-white",
        className := "lg:pt-9 lg:text-3xl lg:font-normal lg:tracking-tight",
        "How did we do?"
      ),
      p(
        className := "text-gray-light",
        className := "lg:tracking-tight lg:leading-relaxed",
        "Please let us know how we did with your support request. All feedback is appreciated to help us improve our offering!"
      ),
      div(
        className := "flex flex-row justify-between py-6 w-full",
        className := "lg:py-8 lg:pb-10",
        votes.map(vote =>
          renderRatingSelector(vote, setVote, isSelectedVoteSignal(vote))
        )
      ),
      button(
        className := "w-full h-12 tracking-widest text-white rounded-full bg-orange",
        className := "duration-300 hover:bg-white hover:text-orange",
        className := "lg:h-14",
        onClick --> Observer(_ => {
          // use Fetch to send results to server here
          markSubmitted(())
        }),
        "SUBMIT"
      )
    )
  }

  def renderRatingSelector(
      vote: Int,
      setVote: Int => Unit,
      isSelectedSignal: Signal[Boolean]
  ) = {
    button(
      className := "flex justify-center items-center w-12 h-12 rounded-full",
      className := "lg:w-14 lg:h-14 lg:text-lg lg:font-semibold",
      className <-- isSelectedSignal.map {
        case true  => "bg-gray-medium text-white"
        case false => "bg-gray-dark text-gray-medium"
      },
      className := "duration-150 hover:text-white hover:bg-orange",
      onClick --> Observer.apply(_ => setVote(vote)),
      s"$vote"
    )
  }

  def renderThankYouUI(finalRatingOpt: Option[Int]): Element = {
    val finalRating = finalRatingOpt.getOrElse({
      println("Error, no was rating selected.")
      1
    })
    div(
      className := "flex flex-col items-center py-10 px-8",
      className := "lg:h-[415px] lg:w-[412px] lg:rounded-[2rem]",
      className := "text-white bg-gradient-to-b rounded-2xl from-blue-dark to-blue-very-dark h-[350px] w-[325px]",
      img(
        src := "/images/illustration-thank-you.svg",
        role := "img",
        alt := "",
        className := "h-28",
        className := "lg:h-32"
      ),
      div(
        className := "my-6 w-auto rounded-full bg-[#282F39] text-[#A76A34]",
        className := "lg:mt-10",
        p(
          className := "py-2 px-3 text-[#D58755]",
          className := "lg:py-2 lg:px-4",
          s"You selected $finalRating out of 5 "
        )
      ),
      h1(
        className := "py-2 text-2xl font-bold",
        className := "lg:pt-4 lg:font-normal lg:text-[1.8rem]",
        "Thank you!"
      ),
      p(
        className := "py-1 leading-relaxed text-center text-gray-400 text-[.9rem]",
        className := "lg:px-2 lg:pt-3 lg:leading-[1.7rem] lg:text-[.95rem]",
        "We appreciate you taking the time to give a rating. If you ever need more support, don’t hesitate to get in touch! "
      )
    )
  }

  def renderAttribution(): Element = {
    footerTag(
      className := "absolute inset-x-0 bottom-2 h-4 text-white attribution",
      role := "contentinfo",
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
