package industries.sunshine.news.pages.front

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.news.pages.front.Models.StoryCard

object NewStoriesComponent {
  def render(newStories: List[StoryCard]): Element = {
    div(
      className := "px-5 bg-very-dark-blue",
      h1(
        className := "pt-5 text-3xl font-bold text-soft-orange",
        "New"
      ),
      div(
        className := "grid grid-cols-1 text-white divide-y divide-grayish-blue",
        newStories.map(renderStoryHeadline(_))
      )
    )
  }

  private def renderStoryHeadline(story: StoryCard): Element = {
    div(
      className := "grid py-6",
      h1(
        className := "pb-2 text-xl font-extrabold",
        story.title),
      p(
        className := "text-grayish-blue",
        story.description)
    )
  }
}
