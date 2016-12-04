package autowire

import upickle.Js
import upickle.default._

/**
  * Created by cfpdiniz on 04/12/2016.
  */
object AutowireServer extends autowire.Server[Js.Value, Reader, Writer]{
  def read[Result: Reader](p: Js.Value) = upickle.default.readJs[Result](p)
  def write[Result: Writer](r: Result) = upickle.default.writeJs(r)
}
