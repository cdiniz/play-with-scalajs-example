package example.components

import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import example.services.UpdateMotd


object Button {

  case class Props(onClick: Callback)

  val component = ReactComponentB[Props]("Button")
    .renderPC((_, p, c) =>
      <.button(^.tpe := "button", ^.onClick --> p.onClick, c)
    ).build

  def apply(props: Props, children: ReactNode*) = component(props, children: _*)
  def apply() = component
}

object Panel {

  case class Props(heading: String)

  val component = ReactComponentB[Props]("Panel")
    .renderPC((_, p, c) =>
      <.div(
        <.div(p.heading),
        <.div(c)
      )
    ).build

  def apply(props: Props, children: ReactNode*) = component(props, children: _*)
  def apply() = component
}


/**
  * This is a simple component demonstrating how to display async data coming from the server
  */
object Motd {

  // create the React component for holding the Message of the Day
  val Motd = ReactComponentB[ModelProxy[Pot[String]]]("Motd")
    .render_P { proxy =>
      Panel(Panel.Props("Message of the day"),
        // render messages depending on the state of the Pot
        proxy().renderPending(_ > 500, _ => <.p("Loading...")),
        proxy().renderFailed(ex => <.p("Failed to load")),
        proxy().render(m => <.p(m)),
        Button(Button.Props(proxy.dispatchCB(UpdateMotd())), " Update")
      )
    }
    .componentDidMount(scope =>
      // update only if Motd is empty
      Callback.when(scope.props.value.isEmpty)(scope.props.dispatchCB(UpdateMotd()))
    )
    .build

  def apply(proxy: ModelProxy[Pot[String]]) = Motd(proxy)
}
