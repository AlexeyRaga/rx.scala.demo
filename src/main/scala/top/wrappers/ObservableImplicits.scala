package top
package wrappers

import rx.lang.scala.Observable
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.Concurrent
import scala.concurrent.ExecutionContext.Implicits.global

object ObservableImplicits {

  implicit class RichObservable[T](val observable: Observable[T]) {
    def asEnumerator = Concurrent.unicast[T] { chan =>
      observable.subscribe(new ChannelObserver(chan))
    }
  }

  class ChannelObserver[T](chan: Channel[T]) extends rx.Observer[T] {
    def onNext(arg: T): Unit = chan.push(arg)
    def onCompleted(): Unit = chan.end()
    def onError(e: Throwable): Unit = chan.end(e)
  }
}
