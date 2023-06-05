package industries.sunshine.news.pages.front

import industries.sunshine.news.pages.front.Models.StoryCard
import com.raquo.laminar.api.L.{*, given}

object FeaturedStoryComponent {
  /**
   * Main story, with illustration
   * mobile design: illustration on top, wide and tall,
   *     header and description below
   * desktop design: illustration on top, tall and very wide,
   * header (grid height 2) below on the left, description on the right
   *
   */
  def render(featuredStorySignal: StoryCard) = {
    div(
      className := "grid gap-8 grid-cols-[minmax(var(--col-min-width),_1fr)]",
      className := "md:grid-cols-[repeat(2,_minmax(var(--col-min-width),_1fr))]",
      div(
        className := "w-full h-[300px]",
        className := "md:col-span-2 md:items-start",
        img(
          src := featuredStorySignal.illustrationUrl,
          alt := "",
          className := "object-cover w-full h-full",
        ),
      ),
        h1(
          featuredStorySignal.title,
          className := "py-5 font-extrabold text-[2.8rem] leading-[2.8rem]",
          className := "md:row-span-2 md:py-2 md:text-6xl",
        ),
      p(
        featuredStorySignal.description,
        className := "pb-6 leading-relaxed text-dark-grayish-blue",
        className := "md:py-1 md:pr-4",
      ),
      button(
        "Read More",
        className := "w-48 h-12 bg-soft-red",
        className := "font-bold uppercase border tracking-[0.25rem] text-off-white border-soft-orange",
        className := "hover:border-0 hover:bg-very-dark-blue",
      ),
    )
  }
}
