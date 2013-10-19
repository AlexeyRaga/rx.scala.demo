package top

import org.apache.http.nio.client.methods.HttpAsyncMethods
import org.apache.http.impl.nio.client.HttpAsyncClients
import rx.apache.http.ObservableHttp
import wrappers.JavaObsImplicits.RichJavaObs

trait TwitterClientComp extends AuthComp {

  lazy val twitterClient  = new TwitterClient

  class TwitterClient {

    val httpClient = HttpAsyncClients.createDefault()

    def start() = httpClient.start()
    def close() = httpClient.close()

    def get(url: String) = {
      val requestProducer = HttpAsyncMethods.createGet(url)
      val request = requestProducer.generateRequest()

      auth.sign(request)

      val rp = HttpAsyncMethods.create(requestProducer.getTarget, request)
      val javaObs = ObservableHttp.createRequest(rp, httpClient).toObservable
      javaObs.asScala
    }

    def get2(url: String) = {
      val requestProducer = HttpAsyncMethods.createGet(url)
      auth.sign(requestProducer.generateRequest())
      val javaObs = ObservableHttp.createRequest(requestProducer, httpClient).toObservable
      javaObs.asScala
    }

  }
}
