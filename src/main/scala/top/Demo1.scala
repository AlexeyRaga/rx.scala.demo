package top

import top.plays.PlayTwitterClientComp
import top.utils.GenObserver
import play.api.libs.ws.WS
import top.wrappers.RequestHolderImplicits.RichRequestHolder
import play.api.libs.iteratee.{Iteratee, Enumeratee}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationLong
import top.wrappers.FutureImplicits.RichFuture
import rx.lang.scala._
import top.wrappers.ObservableImplicits.RichObservable
import play.api.libs.json.JsObject

class Demo1 extends PlayTwitterClientComp {

  val url = "https://stream.twitter.com/1.1/statuses/sample.json"

  def names = playTwitterClient.getJsonStream(url)
    .map(x => (x \ "user" \ "screen_name").asOpt[String])
    .filter(_.isDefined)
    .map(_.get)

  def samples = names.buffer(3)

  def res = samples.map { x =>
    println(x)
    s"Hello $x"
  }.onErrorReturn { ex =>
    ex.printStackTrace()
    "error"
  }

  def demo() = {
    res.subscribe(new GenObserver("GGGGGGGGG"))
//    Thread.sleep(1000)
//    res.subscribe(new GenObserver("Haha1"))
  }

}

class Demo2 {

  val url = "http://en.lichess.org/stream"

  def moves = WS
    .url(url)
    .textStream.map { x =>
      println(x)
      x
    }

  def samples = moves.map(_.split(' ').to[Seq]).take(10)

  def demo() = {
    samples.subscribe(println(_))
  }

}

class Demo3 {

  val url = "http://en.lichess.org/stream"

  def moves = WS
    .url(url)
    .enumerate
    .map(new String(_))
    .map { x =>
      println(x)
      x
    }

  val take10 = Enumeratee.take[Seq[String]](10)
  def samples = moves.map(x => x.split(' ').to[Seq]).through(take10)
  val printer = Iteratee.foreach[Seq[String]](println)
  def demo() = {
    samples.run(printer)
  }

}

class Demo4 {

  val url = "http://en.lichess.org/stream"

  def moves = WS
    .url(url)
    .textStream
    .map(Move.make)

  def samples = moves
    .groupBy(_.gameId)
    .flatMap(_._2.take(4).toSeq)

  def demo() = samples
    .take(3)
    .finallyDo(() => println("done"))
    .subscribe(println(_))

}

class Demo5 {

  val url = "http://en.lichess.org/stream"

  def moves = WS
    .url(url)
    .textStream
    .map(Move.make)

  def samples = moves
    .window(5000.millis, 500.millis)
    .flatMap(_.length)
    .map(_ / 5)

  def demo() = samples
    .take(10)
    .finallyDo(() => println("done"))
    .subscribe(println(_))

}

class Demo6 {

//  val url = "http://api.openweathermap.org/data/2.1/find/station?lat=55&lon=37&cnt=10"
  val url = "http://api.openweathermap.org/data/2.1/find/station"

//  def data = WS
//    .url(url)
//    .withQueryString("lat" -> "55", "lon" -> "37", "cnt" -> "10")
//    .get()
//    .asObservable
//    .map(x => Result(x.json.as[JsObject]))

  val locations = for {
    lat <- -30 to 60 by 5
    lon <- 20 to 70 by 10
  } yield Location(lat, lon)

  def data = Observable(locations: _*).map(_.getWeatherData).concat

  def demo() = {
    data
      .map(_.temperatures)
      .onErrorReturn(_ => Seq())
      .finallyDo(() => println("done"))
      .subscribe(println(_))
  }

}

case class Move(gameId: String, move: String, ip: String)
object Move {
  def make(chunk: String) = chunk.split(' ') match {
    case Array(gameId, move, ip) => new Move(gameId, move, ip)
    case _ => throw new RuntimeException(s"can not parse $chunk")
  }
}

case class WeatherData(json: JsObject) {
  def points = (json \ "list").as[Seq[JsObject]].map(Item)
  def temperatures = points.map(_.temp)
}

case class Item(json: JsObject) {
  def temp = (json \ "main" \ "temp").as[Double]
}

case class Location(lat: Int, lon: Int) {
  val weatherUrl = "http://api.openweathermap.org/data/2.1/find/station"
  def getWeatherData = WS
    .url(weatherUrl)
    .withQueryString("lat" -> lat.toString, "lon" -> lon.toString)
    .get()
    .asObservable
    .map(x => WeatherData(x.json.as[JsObject]))

}
