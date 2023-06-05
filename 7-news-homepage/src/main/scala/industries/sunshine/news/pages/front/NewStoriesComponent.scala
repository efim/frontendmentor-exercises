package industries.sunshine.news.pages.front

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.news.pages.front.Models.StoryCard

object NewStoriesComponent {
  def render(newStories: List[StoryCard]) = {
    div(
      className := "px-5 md:py-0 md:px-6 bg-very-dark-blue",
      h1(
        className := "pt-7 text-3xl font-bold text-soft-orange",
        className := "md:pt-9 md:text-5xl",
        "New"
      ),
      div(
        className := "grid grid-cols-1 text-white divide-y divide-grayish-blue",
        newStories.map(renderStoryHeadline(_))
      )
    )
  }

  private def renderStoryHeadline(story: StoryCard) = {
    div(
      className := "grid py-8",
      a(
        href := "",
        h1(
          className := "pb-2 text-xl font-extrabold hover:text-soft-orange",
          story.title
        )
      ),
      p(className := "text-grayish-blue", story.description)
    )
  }
}
