package top

import org.scalatest.{OneInstancePerTest, MustMatchers, FlatSpec}
import utils.GenObserver
import rx.lang.scala.Scheduler
import rx.lang.scala.concurrency.Schedulers

class A {
  object Assembly extends TwitterProcessorComp
  import Assembly._

  def samples = twitterProcessor.names.buffer(3).take(10)

  val res = samples.map { x =>
    println(x)
    s"Hello $x"
  }.onErrorReturn(_ => "error")

  twitterClient.start()
  val s1 = Schedulers.threadPoolForIO
  val s2 = Schedulers.currentThread
  val s3 = Schedulers.currentThread

  res.observeOn(s1)

  def demo() = {
    val g1 = new GenObserver("GGGGGGGGG")
    val r1 = res.subscribe(g1, s1)
    val h1 = new GenObserver("Haha1")
    val r2 = res.subscribe(h1, s1)

  }

}

class TwitterProcessorCompTest extends FlatSpec with MustMatchers {

  new A().demo()

  it should "do" in {

//    res.subscribe(_ => println("dummy"))
//    res.subscribe(println(_))

//    Thread.sleep(2000)
//    twitterClient.close()
  }
}
