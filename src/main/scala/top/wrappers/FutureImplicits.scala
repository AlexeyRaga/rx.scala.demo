package top.wrappers

import rx.lang.scala._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FutureImplicits {

  implicit class RichFuture[T](val future: Future[T]) extends AnyVal {

    def asObservable = Observable { observer: Observer[T] =>

      future.onComplete {
        case Success(v) =>
          observer.onNext(v)
          observer.onCompleted()
        case Failure(e) =>
          observer.onError(e)
      }

      new Subscription {
        override def unsubscribe() = {}
      }
    }
  }
}
