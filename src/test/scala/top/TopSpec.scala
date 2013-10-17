package top

import org.scalatest.FlatSpec
import rx.lang.scala.Observable
import scala.concurrent._
import ExecutionContext.Implicits.global

class TopSpec extends FlatSpec {

  it should "do" in {

    def squareWithEffect(x: Int) = {
      print(s"$x -> "); x * x
    }

    val seq = Seq(1, 2, 3, 4, 5)

    val observable = Observable(seq: _*)
    val resultObs = observable.map(squareWithEffect)
    resultObs.subscribe(onNext = println(_))

    println("##############")

    val resultF = seq.map(x => future(squareWithEffect(x)))
    resultF.foreach(_.onComplete{
      case x => println(x)
    })

    println("##############")

    val resultFut = Future.traverse(seq)(x => future(squareWithEffect(x)))
    resultFut.onSuccess {
      case x => println(x)
    }


  }
}
