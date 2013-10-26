package top
package apaches

import org.apache.http.nio.client.methods.HttpAsyncMethods
import org.apache.http.impl.nio.client.HttpAsyncClients
import rx.apache.http.ObservableHttp
import wrappers.JavaObsImplicits.RichJavaObs
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import top.secret.TwitterTokens
import play.api.libs.json.{JsObject, Json}

trait ApacheTwitterClientComp {

  lazy val apacheOAuthClient  = new ApacheOAuthClient

  class ApacheOAuthClient {

    val consumer = new CommonsHttpOAuthConsumer(TwitterTokens.ConsumerKey, TwitterTokens.ConsumerSecret)
    consumer.setTokenWithSecret(TwitterTokens.AccessToken, TwitterTokens.TokenSecret)

    def sign(request: Any) = consumer.sign(request)

    def getByteStream(url: String) = {
      val httpClient = HttpAsyncClients.createDefault()
      httpClient.start()

      val requestProducer = HttpAsyncMethods.createGet(url)
      sign(requestProducer.generateRequest())

      val javaObs = ObservableHttp.createRequest(requestProducer, httpClient).toObservable
      javaObs.asScala.flatMap(_.getContent.asScala)
    }

    def getJsonStream(url: String) = getByteStream(url).map(bytes => Json.parse(bytes).as[JsObject])

  }
}

