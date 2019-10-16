package com.uchoa.twitter.utils

import android.util.Base64
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.uchoa.twitter.api.TwitterService
import com.uchoa.twitter.model.TweetLink
import com.uchoa.twitter.ui.TwitterAppViewModel
import java.net.URLEncoder

class TwitterAppHelper {

    companion object {

        fun getBasicTwitterAuthorization(): String {
            val twitterUrlApiKey = URLEncoder.encode(Constant.TWITTER_API_KEY, "UTF-8")
            val twitterUrlApiSecret = URLEncoder.encode(Constant.TWITTER_API_SECRET, "UTF-8")
            val twitterKeySecret = "$twitterUrlApiKey:$twitterUrlApiSecret"
            val twitterKeyBase64 =
                Base64.encodeToString(twitterKeySecret.toByteArray(), Base64.NO_WRAP)

            return "Basic $twitterKeyBase64"
        }

        fun getLinksFromTweet(originalText: String): ArrayList<TweetLink> {
            val arrayString = originalText.split(" ")
            val links = ArrayList<TweetLink>()

            for (index in 0 until arrayString.size) {

                val chunk = arrayString[index]

                if (chunk.startsWith(Constant.TWITTER_MENTIONS_SYMBOL)
                    || chunk.startsWith(Constant.TWITTER_HASH_TAG_SYMBOL)
                    || chunk.startsWith(Constant.TWITTER_IMAGE_URL_PREFIX)
                ) {

                    val startIndex = originalText.indexOf(chunk)
                    val endIndex = startIndex + chunk.length

                    val text = originalText.substring(startIndex, endIndex)
                    val url = when {
                        text.startsWith(Constant.TWITTER_MENTIONS_SYMBOL) ->
                            Constant.TWITTER_PROFILE_URL.format(text.substring(1))
                        text.startsWith(Constant.TWITTER_MENTIONS_SYMBOL) ->
                            Constant.TWITTER_HASH_TAG_URL.format(text.substring(1))
                        else -> text
                    }

                    links.add(TweetLink(text, url, startIndex, endIndex))
                }
            }

            return links
        }

        fun getViewModel(
            activity: FragmentActivity,
            service: TwitterService
        ): TwitterAppViewModel {
            return activity.run {
                ViewModelProviders
                    .of(this, viewModelFactory { TwitterAppViewModel(service) })
                    .get(TwitterAppViewModel::class.java)
            }
        }

        private inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
            }
    }
}