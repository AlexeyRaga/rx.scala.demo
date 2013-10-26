package top

import top.plays.PlayTwitterClientComp
import top.utils.GenObserver

class Demo1 extends PlayTwitterClientComp {

  val url = "https://stream.twitter.com/1.1/statuses/sample.json"

  def names = playTwitterClient.getJsonStream(url)
    .map(x => (x \ "user" \ "screen_name").asOpt[String])
    .filter(_.isDefined)
    .map(_.get)

  def samples = names.buffer(3).take(10)

  def res = samples.map { x =>
    println(x)
    s"Hello $x"
  }.onErrorReturn { ex =>
    ex.printStackTrace()
    "error"
  }

  def demo() = {
    res.subscribe(new GenObserver("GGGGGGGGG"))
    Thread.sleep(1000)
    res.subscribe(new GenObserver("Haha1"))
  }

}
