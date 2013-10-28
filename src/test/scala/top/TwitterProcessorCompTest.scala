package top

import org.scalatest.{MustMatchers, FlatSpec}
import top.utils.ObservableFactory

class TwitterProcessorCompTest extends FlatSpec with MustMatchers {

  it should "do" in {
    println("now")
    new Demo6().demo()
//    ObservableFactory.intervals(30, 10, 30).subscribe(println(_))
  }
}
