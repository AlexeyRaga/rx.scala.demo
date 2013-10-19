package top

import play.api.libs.json.{JsObject, Json}
import wrappers.JavaObsImplicits.RichJavaObs

trait TwitterProcessorComp extends TwitterClientComp {

  lazy val twitterProcessor = new TwitterProcessor
  
  class TwitterProcessor {

    val url = "https://stream.twitter.com/1.1/statuses/sample.json"
    
    def observable = twitterClient.get2(url)
    
    def jsons = for {
      response <- observable
      bytes <- response.getContent.asScala
    } yield Json.parse(bytes).as[JsObject]

    def names = jsons
      .map(x => (x \ "user" \ "screen_name").asOpt[String])
      .filter(_.isDefined)
      .map(_.get)

  }
}
