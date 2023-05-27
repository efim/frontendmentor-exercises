package example

object Data {
  final case class CategoryResult(
      name: String,
      score: Int,
      iconPath: String,
      bgColor: String,
      highlightColor: String
      // highlightColorClass: String
  )

  val hardcoded = List(
    CategoryResult(
      "Reaction",
      80,
      "/assets/images/icon-reaction.svg",
      "#FFF6F5",
      "#DD767D"
    ),
    CategoryResult(
      "Memory",
      92,
      "/assets/images/icon-memory.svg",
      "#FFFBF2",
      "#E3B44A"
    ),
    CategoryResult(
      "Verbal",
      61,
      "/assets/images/icon-verbal.svg",
      "#F2FBFA",
      "#29A784"
    ),
    CategoryResult(
      "Visual",
      72,
      "/assets/images/icon-visual.svg",
      "#F3F3FD",
      "#0E1987"
    )
  )
}
