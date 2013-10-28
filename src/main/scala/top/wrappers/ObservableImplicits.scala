package top
package wrappers

import rx.lang.scala.Observable
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.Concurrent
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise

object ObservableImplicits {

  implicit class RichObservable[T](val observable: Observable[T]) {
    def asEnumerator = Concurrent.unicast[T] { chan =>
      observable.subscribe(new ChannelObserver(chan))
    }
    def asFuture = {
      val promise = concurrent.promise[T]()
      observable.subscribe(new PromiseObserver(promise))
      promise.future
    }
  }

  class ChannelObserver[T](chan: Channel[T]) extends rx.Observer[T] {
    def onNext(arg: T): Unit = chan.push(arg)
    def onCompleted(): Unit = chan.end()
    def onError(e: Throwable): Unit = chan.end(e)
  }

  class PromiseObserver[T](promise: Promise[T]) extends rx.Observer[T] {
    def onNext(arg: T): Unit = promise.success(arg)
    def onCompleted(): Unit = promise.failure(new NoSuchElementException("future of an empty observable"))
    def onError(e: Throwable): Unit = promise.failure(e)
  }
}
