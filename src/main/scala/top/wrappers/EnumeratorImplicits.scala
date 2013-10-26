package top
package wrappers

import play.api.libs.iteratee.{Iteratee, Enumerator}
import rx.lang.scala._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object EnumeratorImplicits {

  implicit class RichEnumerator[T](val enumerator: Enumerator[T]) extends AnyVal {

    def asObservable = Observable { observer: Observer[T] =>
      enumerator(Iteratee.foreach(observer.onNext)).onComplete {
        case Success(_) => observer.onCompleted()
        case Failure(e) => observer.onError(e)
      }

      new Subscription {
        override def unsubscribe() = {}
      }
    }
  }
}
