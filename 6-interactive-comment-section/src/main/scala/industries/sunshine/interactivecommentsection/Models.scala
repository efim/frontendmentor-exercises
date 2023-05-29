package industries.sunshine.interactivecommentsection

import java.time.{Instant, OffsetDateTime, ZoneOffset}
import java.util.UUID

object Models {

  final case class AppState(
      currentUser: AppUser,
      comments: Map[String, Comment]
  )

  final case class AppUser(
      username: String,
      image: AppUser.Image
  )
  object AppUser {
    final case class Image(
        png: String,
        webp: String
    )
  }

  /** Displayable communication unit
    */
  final case class Message(
      id: String,
      content: String,
      createdAt: Instant, // TODO calculate relative time from
      score: Int,
      user: AppUser
  )
  final case class Comment(
      message: Message,
      replies: Map[String, Reply]
  )
  object Comment {
    val empty = Comment(
      Message(
      "", "", Instant.EPOCH, 0, AppUser("", AppUser.Image("", ""))), Map.empty)

  }
  final case class Reply(
      message: Message,
      replyingTo: AppUser
  )

  final case class User(s: String)

  val hardcoded = AppState(
    userJuliusomo,
    List(
      Comment(
        message = Message(
          id = "first-message",
          content =
            "Impressive! Though it seems the drag feature could be improved. But overall it looks incredible. You've nailed the design and the responsiveness at various breakpoints works really well.",
          createdAt = Instant.from(
            OffsetDateTime.of(2023, 4, 22, 15, 28, 31, 0, ZoneOffset.UTC)
          ),
          score = 12,
          user = userAmyrobson
        ),
        replies = Map.empty
      ),
      Comment(
        message = Message(
          id = "second-message",
          content =
            "Woah, your project looks awesome! How long have you been coding for? I'm still new, but think I want to dive into React as well soon. Perhaps you can give me an insight on where I can learn React? Thanks!",
          createdAt = Instant.from(
            OffsetDateTime.of(2023, 5, 11, 16, 1, 53, 0, ZoneOffset.UTC)
          ),
          score = 5,
          user = userMaxblagun
        ),
        replies = (List(
          Reply(
            message = Message(
              id = newUUID(),
              content =
                "If you're still new, I'd recommend focusing on the fundamentals of HTML, CSS, and JS before considering React. It's very tempting to jump ahead but lay a solid foundation first.",
              createdAt = Instant.from(
                OffsetDateTime.of(2023, 5, 16, 1, 13, 3, 0, ZoneOffset.UTC)
              ),
              score = 4,
              user = userRamsesmiron
            ),
            replyingTo = userRamsesmiron
          ),
          Reply(
            message = Message(
              id = newUUID(),
              content =
                "I couldn't agree more with this. Everything moves so fast and it always seems like everyone knows the newest library/framework. But the fundamentals are what stay constant.",
              createdAt = Instant.from(
                OffsetDateTime.of(2023, 5, 26, 14, 3, 38, 0, ZoneOffset.UTC)
              ),
              score = 2,
              user = userJuliusomo
            ),
            replyingTo = userRamsesmiron
          )
        ).map(reply => (reply.message.id, reply)).toMap)
      )
    ).map(comment => (comment.message.id, comment)).toMap
  )

  lazy val userAmyrobson = AppUser(
    "amyrobson",
    AppUser.Image(
      "./images/avatars/image-amyrobson.png",
      "./images/avatars/image-amyrobson.webp"
    )
  )

  lazy val userMaxblagun = AppUser(
    "maxblagun",
    AppUser.Image(
      "./images/avatars/image-maxblagun.png",
      "./images/avatars/image-maxblagun.webp"
    )
  )

  lazy val userRamsesmiron = AppUser(
    "ramsesmiron",
    AppUser.Image(
      "./images/avatars/image-ramsesmiron.png",
      "./images/avatars/image-ramsesmiron.webp"
    )
  )

  lazy val userJuliusomo = AppUser(
    "juliusomo",
    AppUser.Image(
      "./images/avatars/image-juliusomo.png",
      "./images/avatars/image-juliusomo.webp"
    )
  )

  def newUUID(): String = UUID.randomUUID().toString()
}
