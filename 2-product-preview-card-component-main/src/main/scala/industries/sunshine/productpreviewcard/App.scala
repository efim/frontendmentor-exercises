package industries.sunshine.productpreviewcard

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {

  def appElement(): Element =
    div(
      mainTag(
        className := "flex relative flex-col w-screen h-screen bg-cream",
        role := "main",
        div(
          className := "flex flex-grow justify-center items-center",
          renderProductPreviewCard(hardcodedProduct)
        ),
      ),
      renderAttribution()
    )

  def renderProductPreviewCard(product: ProductDescription) = {

    val windowResizeEvents = new EventBus[Unit]
    dom.window.addEventListener(
      "resize",
      _ => windowResizeEvents.writer.onNext(())
    )
    val windowWidthStream: Signal[Int] = windowResizeEvents.events
      .map(_ => dom.window.innerWidth.toInt)
      .startWith(dom.window.innerWidth.toInt)
    val isMobileStream: Signal[Boolean] = windowWidthStream.map(_ <= 1024)

    val dynamicImage = isMobileStream.map {
      case true =>
        img(
          src := product.mobileImg,
          role := "img",
          alt := product.photoAltTest,
          className := "rounded-t-lg"
        )
      case false =>
        img(
          src := product.desktopImg,
          role := "img",
          alt := product.photoAltTest,
          className := "rounded-l-xl h-[450px]"
        )
    }

    div(
      className := "flex flex-col bg-white rounded-lg lg:flex-row w-[350px] h-[600px]",
      className := "lg:rounded-xl lg:w-[600px] lg:h-[450px]",
      child <-- dynamicImage,
      div(
        className := "flex flex-col p-7 lg:p-9",
        h1( // CATEGORY NAME
          product.category,
          className := "text-sm text-gray-500 uppercase tracking-[.3rem]"
        ),
        h2( // TITLE
          product.title,
          className := "py-3 font-serif text-4xl font-bold lg:py-5 lg:leading-none"
        ),
        p( // DESCRIPTION
          product.description,
          className := "text-base text-gray-500 lg:pt-2 lg:leading-relaxed"
        ),
        div( // PRICES
          className := "grid grid-cols-2 items-center pt-7 pb-5 lg:grid-cols-5 lg:pb-8",
          p(
            product.price,
            className := "font-serif text-4xl lg:col-span-3 text-dark-cyan"
          ),
          product.oldPrice match {
            case Some(crossedPrice) =>
              p(crossedPrice, className := "text-sm text-gray-500 line-through")
            case None => emptyNode
          }
        ),
        button(
          img(
            src := "/images/icon-cart.svg",
            aria.hidden := true,
            alt := "",
            className := "px-4 pl-0 h-4 lg:pr-3"
          ),
          "Add to Cart",
          className := "w-full rounded-lg h-[3.25rem] bg-dark-cyan",
          className := "text-sm font-bold text-white",
          className := "flex flex-row justify-center items-center",
          className := "duration-300 active:scale-105 hover:bg-darkest-cyan"
        )
      )
    )
  }

  def renderAttribution(): Element = {
    footerTag(
      role := "contentinfo",
      className := "absolute inset-x-0 bottom-2 attribution",
      "Challenge by ",
      a(
        href := "https://www.frontendmentor.io?ref=challenge",
        target := "_blank",
        "Frontend Mentor"
      ),
      " Coded by ",
      a(href := "#", "Your Name Here")
    )
  }

  final case class ProductDescription(
      mobileImg: String,
      desktopImg: String,
      category: String,
      title: String,
      description: String,
      price: String,
      oldPrice: Option[String],
      photoAltTest: String
  )
  val hardcodedProduct = ProductDescription(
    "/images/image-product-mobile.jpg",
    "/images/image-product-desktop.jpg",
    "Perfume",
    "Gabrielle Essence Eau De Parfum",
    "A floral, solar and voluptuous interpretation composed by Olivier Polge, Perfumer-Creator for the House of CHANEL.",
    "$149.99",
    Some("$169.99"),
    "CHANEL perfume in a rectangular glass bottle."
  )
}
