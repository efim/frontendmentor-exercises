package industries.sunshine.todolist

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.codecs.StringAsIsCodec

object Background {
  def render() = {
    val pictureTag = htmlTag("picture")
    val srcsetProp = htmlProp("srcset", StringAsIsCodec)

    // wow, thank you web dev simplified:
    // https://blog.webdevsimplified.com/2023-05/responsive-images/
    // and it turns out there's big article from Mozilla:
    // https://developer.mozilla.org/en-US/docs/Learn/HTML/Multimedia_and_embedding/Responsive_images
    div(
      className := "fixed w-screen h-screen -z-10 bg-very-light-gray",
      pictureTag(
        sourceTag(
          media := "(max-width: 375px)",
          srcsetProp := "/images/bg-mobile-light.jpg",
        ),
        img(
            src := "/images/bg-desktop-light.jpg",
            className := "w-full",
        )
      )
    )
  }
}
