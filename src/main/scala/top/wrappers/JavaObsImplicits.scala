package top
package wrappers

import rx.lang.scala.Observable

object JavaObsImplicits {

  implicit class RichJavaObs[T](val javaObs: rx.Observable[T]) extends AnyVal {

    def asScala = Observable(javaObs)
  }
}
