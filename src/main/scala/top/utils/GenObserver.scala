package top
package utils

import rx.Observer

class GenObserver(prefix: String) extends Observer[String] {

  var counter = 0

  def onNext(args: String) {
    counter += 1
    println(s"$prefix ==> $args")
    Thread.sleep(1000)
  }

  def onError(e: Throwable) {
    e.printStackTrace()
    println(s"error with::: $counter")
  }

  def onCompleted() {
    println(s"done with::: $counter")
  }
}
