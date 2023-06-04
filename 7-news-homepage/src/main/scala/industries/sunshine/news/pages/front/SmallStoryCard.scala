package industries.sunshine.news.pages.front

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.news.pages.front.Models.StoryCard

object SmallStoryCard {
  def renderList(stories: List[StoryCard]): List[Element] = {
        stories.zipWithIndex
          .map((story, index) => story -> (index + 1)).map(render)
  }

  def render(storyInfo: (StoryCard, Int)): Element = {
    val (story, index) = storyInfo
    div(
      className := "grid gap-5 grid-cols-[1fr_2fr]",
      img(
        src := story.illustrationUrl,
        alt := ""
      ),
      div(
        p(
          className := "text-3xl font-bold text-grayish-blue",
          "%02d".format(index)
        ),
        h1(
          className := "py-2 text-xl font-bold",
          story.title
        ),
        p(
          className := "leading-relaxed text-dark-grayish-blue",
          story.description
        )
      )
    )
  }
}
