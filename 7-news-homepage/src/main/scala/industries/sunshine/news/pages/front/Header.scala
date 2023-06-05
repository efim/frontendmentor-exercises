package industries.sunshine.news.pages.front

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.scalajs.dom.HTMLDialogElement

object PageHeader {

  def render() = {
    val resizeEventBus = EventBus[Unit]()
    dom.window.addEventListener("resize", _ => resizeEventBus.writer.onNext(()))
    val screenSizesStream =
      resizeEventBus.events.map(_ => dom.window.innerWidth.toInt)
    val isMobileSignal = screenSizesStream
      .map(_ < 1024)
      .distinct
      .startWith(dom.window.innerWidth < 1024)

    lazy val mobileMenu = renderMobileControl()
    lazy val desktopMenu = renderDesktopControls()

    div(
      className := "flex flex-row items-center py-8 h-14",
      className := "md:pt-28 md:pb-16",
      // className := "relative",
      div(
        className := "flex-1",
        img(
          src := "/images/logo.svg",
          alt := "Company logo - letter 'W'",
          className := "w-12 md:w-16"
        )
      ),
      div(
        child <-- isMobileSignal.map(if (_) mobileMenu else desktopMenu)
      )
    )
  }

  // // would be cool to use dialog
  // // but couldn't get it to align to the right and be full height
  // private def defineMobileModalMenu() = {
  //   dialogTag(
  //     className := "absolute right-0 my-0 w-2/3 h-[1500px]",
  //     form(
  //       className := "grid space-y-10 text-lg text-dark-grayish-blue",
  //       button("Close", formMethod := "dialog"),
  //       a(href := "", "Home"),
  //       a(href := "", "New"),
  //       a(href := "", "Popular"),
  //       a(href := "", "Trending"),
  //       a(href := "", "Categories")
  //     )
  //   )
  // }

  private val isModalOpen = Var(false)
  private def defineMobileModalMenu() = {
    div(
      className <-- isModalOpen.signal.map(if (_) "visible" else "hidden"),
      div( // Dimming layer
        className := "fixed inset-0 bg-black opacity-50",
        // className <-- isModalOpen.signal.map(if (_) "grid" else "hidden"),
        onClick.preventDefault --> Observer(_ =>
          isModalOpen.set(false)
        ) // Optional: close the modal when clicking on the dimming layer
      ),
      div( // Our actual modal controls
        className := "fixed top-0 right-0 p-8 w-3/4 h-screen bg-off-white",
        // className := "absolute top-0 right-0 p-8 w-3/4 h-screen bg-off-white",
        className <-- isModalOpen.signal.map(if (_) "grid" else "hidden"),
        // grid to align close to the top, links to 3rd row after some offset
        className := "grid grid-rows-[auto_100px_auto_1fr]",
        button(
          className := "justify-self-end",
          img(
            src := "/images/icon-menu-close.svg",
            alt := "close the menu"
          ),
          onClick.preventDefault --> Observer(_ => isModalOpen.set(false))
        ),
        div( // these are control links
          className := "grid space-y-6 text-xl text-very-dark-blue",
          className := "row-start-3",
          a(href := "", "Home"),
          a(href := "", "New"),
          a(href := "", "Popular"),
          a(href := "", "Trending"),
          a(href := "", "Categories")
        )
      )
    )
  }

  private def renderMobileControl() = {
    val modalMenu = defineMobileModalMenu()
    div(
      button(
        img(
          src := "/images/icon-menu.svg",
          alt := "navigation menu"
        ),
        onClick --> Observer(_ => isModalOpen.writer.onNext(true))
      ),
      modalMenu
    )
  }
  private def renderDesktopControls() = div(
    className := "grid grid-flow-col space-x-10 text-lg text-dark-grayish-blue",
    a(href := "", "Home", className := "hover:text-soft-red"),
    a(href := "", "New", className := "hover:text-soft-red"),
    a(href := "", "Popular", className := "hover:text-soft-red"),
    a(href := "", "Trending", className := "hover:text-soft-red"),
    a(href := "", "Categories", className := "hover:text-soft-red"),
  )
}
