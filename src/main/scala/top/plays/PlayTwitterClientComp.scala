package top
package plays

import wrappers.RequestHolderImplicits.RichRequestHolder
import top.secret.TwitterTokens
import play.api.libs.oauth.{RequestToken, ConsumerKey, OAuthCalculator}
import play.api.libs.ws.WS

trait PlayTwitterClientComp {

  lazy val playTwitterClient = new PlayTwitterClient
  
  class PlayTwitterClient {

    val oAuthCalculator = OAuthCalculator(
      ConsumerKey(TwitterTokens.ConsumerKey, TwitterTokens.ConsumerSecret),
      RequestToken(TwitterTokens.AccessToken, TwitterTokens.TokenSecret)
    )

    def getJsonStream(url: String) = WS
      .url(url)
      .sign(oAuthCalculator)
      .jsonStream

  }
}
