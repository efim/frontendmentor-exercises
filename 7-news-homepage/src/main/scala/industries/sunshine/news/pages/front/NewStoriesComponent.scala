package industries.sunshine.news.pages.front

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.news.pages.front.Models.StoryCard

object NewStoriesComponent {
  def render(newStories: Signal[List[StoryCard]]): Element = {
    div(
      className := "p-5 bg-very-dark-blue",
      h1(
        className := "text-3xl font-bold text-soft-orange",
        "New"
      ),
      div(
        className := "grid text-white divide-y",
        div("story1"),
        div("story2"),
        div("story3")
      )
    )
  }

  private def renderStoryHeadline(storySignal: Signal[StoryCard]): Element = {
        div(
          className := "grid",
          h1(child.text <-- storySignal.map(_.title)),
          p(child.text <-- storySignal.map(_.description)),
        )
  }
}
