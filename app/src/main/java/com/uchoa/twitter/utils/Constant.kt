package com.uchoa.twitter.utils

class Constant {

    companion object {
        const val APP_TAG = "TwitterFeelings"

        const val TWITTER_BASE_URL = "https://api.twitter.com/"
        const val TWITTER_TOKEN_URL = "oauth2/token"
        const val TWITTER_TWEETS_URL = "1.1/statuses/user_timeline.json?"
        const val TWITTER_USER_URL = "1.1/users/show.json?"
        const val TWITTER_PROFILE_URL = "https://twitter.com/%s"
        const val TWITTER_HASH_TAG_URL = "https://twitter.com/hashtag/%s?src=hashtag_click"

        const val TWITTER_MENTIONS_SYMBOL = "@"
        const val TWITTER_HASH_TAG_SYMBOL = "#"
        const val TWITTER_IMAGE_URL_PREFIX = "http"

        const val TWITTER_API_KEY = "9xyEM1oCyTvni1ceBtbAYwPVu"
        const val TWITTER_API_SECRET = "oJcppNrhb1kNC4hLuVGSotffoa9gvcgiG2J09Dp7ZysTBmJovY"

        const val GOOGLE_NATURAL_LANGUAGE_API_KEY = "AIzaSyD35DqXPdlqgZTPCGN-Frys0uoHMTA58Qk"
    }
}