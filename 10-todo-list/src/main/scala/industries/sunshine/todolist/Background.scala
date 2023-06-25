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
      className := "fixed w-screen h-screen -z-10 bg-very-light-grayish-blue",
      className := "dark:bg-dt-very-dark-blue",
      pictureTag(
        sourceTag(
          media := "(max-width: 768px)",
          srcsetProp <-- Theme.isDarkThemeSignal.map(
            if (_) "/images/bg-mobile-dark.jpg"
            else "/images/bg-mobile-light.jpg"
          )
        ),
        img(
          src <-- Theme.isDarkThemeSignal.map(
            if (_) "/images/bg-desktop-dark.jpg"
            else "/images/bg-desktop-light.jpg"
          ),
          alt := "",
          className := "w-full"
        )
      )
    )
  }
}
