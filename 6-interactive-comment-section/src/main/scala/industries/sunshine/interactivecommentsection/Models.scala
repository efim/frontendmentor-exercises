package industries.sunshine.interactivecommentsection

import java.time.{Instant, OffsetDateTime, ZoneOffset}

object Models {

  final case class AppState(
      currentUser: User,
      comments: List[Comment]
  )

  final case class User(
      username: String,
      image: User.Image
  )
  object User {
    final case class Image(
        png: String,
        webp: String
    )
  }

  /**
   * Displayable communication unit
   */
  trait Message {
    def id: String
    def content: String
    def createdAt: Instant
    def score: Int
    def user: User
  }
  final case class Comment(
      id: String,
      content: String,
      createdAt: Instant, // TODO calculate relative time from
      score: Int,
      user: User,
      replies: List[Reply]
  ) extends Message
  final case class Reply(
      id: String,
      content: String,
      createdAt: Instant, // TODO calculate relative time from
      score: Int,
      user: User,
      replyingTo: User
  ) extends Message


  val hardcoded = AppState(
    userJuliusomo,
    List(
      Comment(
        id = "1",
        content =
          "Impressive! Though it seems the drag feature could be improved. But overall it looks incredible. You've nailed the design and the responsiveness at various breakpoints works really well.",
        createdAt = Instant.from(
          OffsetDateTime.of(2023, 4, 22, 15, 28, 31, 0, ZoneOffset.UTC)
        ),
        score = 12,
        user = userAmyrobson,
        replies = List.empty
      ),
      Comment(
        id = "2",
        content =
          "Woah, your project looks awesome! How long have you been coding for? I'm still new, but think I want to dive into React as well soon. Perhaps you can give me an insight on where I can learn React? Thanks!",
        createdAt = Instant.from(
          OffsetDateTime.of(2023, 5, 11, 16, 1, 53, 0, ZoneOffset.UTC)
        ),
        score = 5,
        user = userMaxblagun,
        replies = List(
          Reply(
            id = "3",
            content =
              "If you're still new, I'd recommend focusing on the fundamentals of HTML, CSS, and JS before considering React. It's very tempting to jump ahead but lay a solid foundation first.",
            createdAt = Instant.from(
              OffsetDateTime.of(2023, 5, 16, 1, 13, 3, 0, ZoneOffset.UTC)
            ),
            score = 4,
            user = userRamsesmiron,
            replyingTo = userRamsesmiron
          ),
          Reply(
            id = "4",
            content =
              "I couldn't agree more with this. Everything moves so fast and it always seems like everyone knows the newest library/framework. But the fundamentals are what stay constant.",
            createdAt = Instant.from(
              OffsetDateTime.of(2023, 5, 26, 14, 3, 38, 0, ZoneOffset.UTC)
            ),
            score = 2,
            replyingTo = userRamsesmiron,
            user = userJuliusomo
          )
        )
      )
    )
  )

  val userAmyrobson = User(
    "amyrobsonUser",
    User.Image(
      "./images/avatars/image-amyrobson.png",
      "./images/avatars/image-amyrobson.webp"
    )
  )

  val userMaxblagun = User(
    "maxblagun",
    User.Image(
      "./images/avatars/image-maxblagun.png",
      "./images/avatars/image-maxblagun.webp"
    )
  )

  val userRamsesmiron = User(
    "ramsesmiron",
    User.Image(
      "./images/avatars/image-ramsesmiron.png",
      "./images/avatars/image-ramsesmiron.webp"
    )
  )

  val userJuliusomo = User(
    "juliusomo",
    User.Image(
      "./images/avatars/image-juliusomo.png",
      "./images/avatars/image-juliusomo.webp"
    )
  )

}
