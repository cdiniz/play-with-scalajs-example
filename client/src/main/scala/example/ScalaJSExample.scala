package example
import upickle.default._
import upickle.Js
import org.scalajs.dom
import shared.{Api, Message}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.concurrent.Future
import scala.scalajs.js
import autowire._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react._

object Client extends autowire.Client[Js.Value, Reader, Writer]{
  override def doCall(req: Request): Future[Js.Value] = {
    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = upickle.json.write(Js.Obj(req.args.toSeq:_*))
    ).map(_.responseText)
      .map(upickle.json.read)
  }

  def read[Result: Reader](p: Js.Value) = readJs[Result](p)
  def write[Result: Writer](r: Result) = writeJs(r)
}


object ScalaJSExample extends js.JSApp {
  def main(): Unit = {

    class Backend($: BackendScope[Unit, Message]) {
      def start = Callback.future(
        Client[Api].echo("hello from scalajs").call().map( message => $.modState(m => message)))

      def render(state: Message) =
        <.div(state.message)
    }

    val Hello = ReactComponentB[Unit]("Example")
      .initialState(Message(""))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .build

    Hello() render dom.document.getElementById("root")

  }
}
