package example
import upickle.default._
import upickle.Js
import org.scalajs.dom
import shared.Api
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.concurrent.Future
import scala.scalajs.js
import autowire._

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
    Client[Api].echo("hello from scalajs").call().map( message =>
      dom.document.getElementById("scalajsShoutOut").textContent = message.message
    )
  }
}
