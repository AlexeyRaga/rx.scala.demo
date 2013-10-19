package top

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import secrets._

trait AuthComp {

  lazy val auth = new Auth

  class Auth {
    val consumer = new CommonsHttpOAuthConsumer(TwitterTokens.CONSUMER_KEY, TwitterTokens.CONSUMER_SECRET)
    consumer.setTokenWithSecret(TwitterTokens.ACCESS_TOKEN, TwitterTokens.TOKEN_SECRET)

    def sign(request: Any) = consumer.sign(request)
    def sign(request: String) = consumer.sign(request)
  }
}
