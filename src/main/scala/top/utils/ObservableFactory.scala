package top.utils

import rx.lang.scala.Observable
import scala.concurrent.duration.DurationInt

object ObservableFactory {

  def millis(n: Int) = Observable.interval(n.millis).map(x => (x+1) * n)

  def delay(millis: Int) = this.millis(millis).take(1)

  def intervals(delays: Int*) = Observable(delays.map(delay): _*).concat.scan(0L)(_ + _).drop(1)
}
