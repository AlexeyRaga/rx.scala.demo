package top
package wrappers

import play.api.libs.ws.WS.WSRequestHolder
import play.api.libs.iteratee.Concurrent
import wrappers.EnumeratorImplicits.RichEnumerator
import play.api.libs.json.{JsObject, Json}
import java.lang.String

object RequestHolderImplicits {

  implicit class RichRequestHolder(val requestHolder: WSRequestHolder) extends AnyVal {

    def enumerate = {
      val (iteratee, enumerator) = Concurrent.joined[Array[Byte]]
      requestHolder.get(_ => iteratee)
      enumerator
    }

    def bytesStream = enumerate.asObservable
    def jsonStream = bytesStream.map(bytes => Json.parse(bytes).as[JsObject])
    def textStream = bytesStream.map(bytes => new String(bytes))
  }
}
