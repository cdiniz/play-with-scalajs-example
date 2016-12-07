package example
import example.components.Motd
import example.services.SPACircuit
import japgolly.scalajs.react.ReactDOM
import org.scalajs.dom
import scala.scalajs.js

object ScalaJSExample extends js.JSApp {
  def main(): Unit = {
    SPACircuit.wrap(_.motd)(proxy => ReactDOM.render(Motd(proxy), dom.document.getElementById("root")))
  }
}
