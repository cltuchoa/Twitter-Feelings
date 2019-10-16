package com.uchoa.twitter.api

import com.uchoa.twitter.model.Tweet
import com.uchoa.twitter.model.User
import com.uchoa.twitter.model.response.TweetResponse
import com.uchoa.twitter.model.response.UserResponse

class TwitterMapper {

    fun convertResponseToUser(
        userResponse: UserResponse?,
        tweetResponse: MutableList<TweetResponse>?
    ): User {

        val tweets = arrayListOf<Tweet>()
        tweetResponse?.forEach { it ->
            tweets.add(Tweet(it.text, it.created_at.replace("+0000 ", "")))
        }

        return User(
            userResponse?.id ?: "",
            userResponse?.name ?: "",
            userResponse?.profile_image_url_https ?: "",
            userResponse?.profile_banner_url ?: "",
            tweets
        )
    }
}