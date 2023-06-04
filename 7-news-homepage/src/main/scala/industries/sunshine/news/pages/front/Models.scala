package industries.sunshine.news.pages.front

object Models {
  final case class StoryCard(
      title: String,
      description: String,
      illustrationUrl: String
  )

  final case class FrontPageState(
      headliner: StoryCard,
      newArticles: List[StoryCard],
      recommended: List[StoryCard]
  )

  val hardcoded = FrontPageState(
    headliner = StoryCard(
      "The Bright Future of Web 3.0?",
      """  We dive into the next evolution of the web that claims to put the power of the platforms back into the hands of the people.
  But is it really fulfilling its promise? """,
      "/images/image-web-3-desktop.jpg"
    ),
    newArticles = List(
      StoryCard(
        "Hydrogen VS Electric Cars",
        "Will hydrogen-fueled cars ever catch up to EVs?",
        "url"
      ),
      StoryCard(
        "The Downsides of AI Artistry",
        "What are the possible adverse effects of on-demand AI image generation?",
        "url"
      ),
      StoryCard(
        "Is VC Funding Drying Up?",
        "Private funding by VC firms is down 50% YOY. We take a look at what that means.",
        "url"
      )
    ),
    recommended = List(
      StoryCard(
        "Reviving Retro PCs",
        "What happens when old PCs are given modern upgrades?",
        "/images/image-retro-pcs.jpg"
      ),
      StoryCard(
        "Top 10 Laptops of 2022",
        "Our best picks for various needs and budgets.",
        "/images/image-top-laptops.jpg"
      ),
      StoryCard(
        "The Growth of Gaming",
        "How the pandemic has sparked fresh opportunities.",
        "/images/image-gaming-growth.jpg"
      )
    )
  )
}
